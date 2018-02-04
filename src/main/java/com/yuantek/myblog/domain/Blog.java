package com.yuantek.myblog.domain;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotEmpty;

import com.github.rjeschke.txtmark.Processor;

/**
 * JPA =====> 博客实体
 * 
 * @author mi.zhe
 *
 */
@Entity
public class Blog implements Serializable {

	private static final long serialVersionUID = -3163920460767393037L;

	@Id  // 主键
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 主键生成策略,根据数据库自增
	private Long id;
	
	@NotEmpty(message = "标题不能为空")
	@Size(min = 2, max = 50)
	@Column(nullable = false, length = 50) // 映射为字段,值不能为空
	private String title;
	
	@NotEmpty(message = "摘要不能为空")
	@Size(min = 2, max = 300)
	@Column(nullable = false)
	private String summary;
	
	@Lob  // 大对象,映射Mysql的Long Text 类型
	@Basic(fetch=FetchType.LAZY) //懒加载
	@NotEmpty(message = "内容不能为空")
	@Size(min = 2)
	@Column(nullable = false)
	private String content;
	
	@Lob  // 大对象,映射Mysql的Long Text 类型
	@Basic(fetch=FetchType.LAZY) //懒加载
	@NotEmpty(message = "内容不能为空")
	@Size(min = 2)
	@Column(nullable = false)
	private String htmlContent; // 将md转为html

	@OneToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	@Column(nullable = false) // 映射为字段,值不能为空
	@CreationTimestamp // 由数据库自动创建时间
	private Timestamp createTime;
	
	@Column(name="readSize")
	private Integer readSize = 0; // 访问量,阅读量
	
	@Column(name="commentSize")
	private Integer commentSize = 0; // 评论量
	
	@Column(name="voteSize")
	private Integer voteSize = 0; // 点赞量
	
	@Column(name = "tags" , length = 100)
	private String tags; // 标签 多个标签组成的字符串,用英文逗号分隔 "a,b,c"

	//增加评论模块,修改blog类,建立blog与comment的关系 
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	@JoinTable(name="blog_comment",joinColumns = @JoinColumn(name="blog_id",referencedColumnName="id")
				,inverseJoinColumns = @JoinColumn(name="comment_id",referencedColumnName="id"))
	private List<Comment> comments;
	
	//增加点赞模块,修改blog类,建立blog与Vote的关系
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinTable(name="blog_vote",joinColumns = @JoinColumn(name="blog_id",referencedColumnName="id")
				,inverseJoinColumns = @JoinColumn(name="vote_id",referencedColumnName="id"))
	private List<Vote> votes;
	
	// 增加分类模块,修改blog类,建立关系
	@OneToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
	@JoinColumn(name="catalog_id")
	private Catalog catalog;
	
	protected Blog() {
	}

	public Blog(String title, String summary, String content) {
		this.title = title;
		this.summary = summary;
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		this.htmlContent = Processor.process(content); // 讲Markdown 内容转为HTML格式
	}

	public String getHtmlContent() {
		return htmlContent;
	}

//	public void setHtmlContent(String htmlContent) {
//		this.htmlContent = htmlContent;
//	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public Integer getReadSize() {
		return readSize;
	}

	public void setReadSize(Integer readSize) {
		this.readSize = readSize;
	}

	public Integer getCommentSize() {
		return commentSize;
	}

	public void setCommentSize(Integer commentSize) {
		this.commentSize = commentSize;
	}

	public Integer getVoteSize() {
		return voteSize;
	}

	public void setVoteSize(Integer voteSize) {
		this.voteSize = voteSize;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	// up
	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
		// 同时要修改评论量
		this.commentSize = this.comments.size();
	}
	
	/**
	 * 添加评论
	 * @param comment
	 */
	public void addComment(Comment comment) {
		this.comments.add(comment);
		// 也要去修改评论量这个属性的
		this.commentSize = this.comments.size();
	}
	
	/**
	 * 删除评论
	 * @param commentId
	 */
	public void removeComment(Long commentId) {
		for(int index = 0 ; index < this.comments.size() ; index ++) {
			if(comments.get(index).getId().equals(commentId)) { // 这里不应该用 ==  要用equals 回头查查commons组件里面有没有这个工具方法
				this.comments.remove(index);
				break;
			}
		}
		// 同时修改评论量
		this.commentSize = this.comments.size();
	}

	public List<Vote> getVotes() {
		return votes;
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
		// 同时修改点赞量
		this.voteSize = this.votes.size();
	}
	
	/**
	 * 点赞
	 * @param vote
	 * @return
	 */
	public boolean addVote(Vote vote) {
		boolean isExist = false;
		// 判断重复
		for (int index=0; index < this.votes.size(); index ++ ) {
			if (this.votes.get(index).getUser().getId() == vote.getUser().getId()) {
				isExist = true;
				break;
			}
		}
		if (!isExist) {
			this.votes.add(vote);
			this.voteSize = this.votes.size();
		}

		return isExist;
		
	}
	
	/**
	 * 取消点赞
	 * @param voteId
	 */
	public void removeVote(Long voteId) {
		for (int index=0; index < this.votes.size(); index ++ ) {
			if (this.votes.get(index).getId() == voteId) {
				this.votes.remove(index);
				break;
			}
		}
		
		this.voteSize = this.votes.size();
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}
	
}
