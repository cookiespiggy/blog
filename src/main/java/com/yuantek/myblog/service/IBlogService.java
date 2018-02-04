package com.yuantek.myblog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.yuantek.myblog.domain.Blog;
import com.yuantek.myblog.domain.Catalog;
import com.yuantek.myblog.domain.User;

/**
 * 博客服务接口
 * 
 * @author mi.zhe
 *
 */
public interface IBlogService {

	/**
	 * 保存博客
	 * 
	 * @param blog
	 * @return
	 */
	Blog savaBlog(Blog blog);

	/**
	 * 通过id删除博客
	 * 
	 * @param id
	 */
	void removeBlog(Long id);

	/**
	 * 根据id获取Blog
	 * 
	 * @param id
	 * @return
	 */
	Blog getBlogById(Long id);

	/**
	 * 最新查询
	 * 
	 * @param user
	 * @param title
	 * @param pageable
	 * @return
	 */
	Page<Blog> listBlogByTitleVote(User user, String title, Pageable pageable);

	/**
	 * 最热查询
	 * 
	 * @return
	 */
	Page<Blog> listBlogsByTitleVoteAndSort(User user, String title, Pageable pageable);

	/**
	 * 阅读量递增
	 * 
	 * @param id
	 */
	void readingIncrease(Long id);

	// 增加完评论模块,还要在IBlogService中增加关于评论的方法
	/**
	 * 发表评论
	 * 
	 * @param blogId
	 * @param commentContent
	 * @return
	 */
	Blog createComment(Long blogId, String commentContent);

	/**
	 * 删除评论
	 * 
	 * @param blogId
	 * @param commentId
	 */
	void removeComment(Long blogId, Long commentId);

	// 增加完点赞模块,还要在IBlogService中增加关于点赞的方法

	/**
	 * 点赞
	 * 
	 * @param blogId
	 * @return
	 */
	Blog createVote(Long blogId);

	/**
	 * 取消点赞
	 * 
	 * @param blogId
	 * @param voteId
	 */
	void removeVote(Long blogId, Long voteId);

	// 增加完分类模块,还要在IBlogService中增加关于分类的方法

	/**
	 * 根据分类进行查询
	 * 
	 * @param catalog
	 * @param pageable
	 * @return
	 */
	Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable);

}
