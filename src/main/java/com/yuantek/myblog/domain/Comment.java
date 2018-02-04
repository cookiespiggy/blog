package com.yuantek.myblog.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * JPA ===> 评论实体
 * 
 * @author mizhe
 *
 */
@Entity
public class Comment implements Serializable {

	private static final long serialVersionUID = 8961917253374955187L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = "评论内容不能为空")
	@Size(min = 2, max = 500)
	@Column(nullable = false)
	private String content;

	@OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user; // 一条评论对应(属于,所属)一个用户

	@Column(nullable = false) // 映射为字段,值不能为空
	@CreationTimestamp // 由数据库自动创建时间
	private Timestamp createTime;

	protected Comment() {

	}

	public Comment(String content, User user) {
		super();
		this.content = content;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

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
}
