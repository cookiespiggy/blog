package com.yuantek.myblog.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.yuantek.myblog.domain.Blog;
import com.yuantek.myblog.domain.Catalog;
import com.yuantek.myblog.domain.Comment;
import com.yuantek.myblog.domain.User;
import com.yuantek.myblog.domain.Vote;
import com.yuantek.myblog.domain.es.EsBlog;
import com.yuantek.myblog.repository.BlogRepository;
import com.yuantek.myblog.service.IBlogService;
import com.yuantek.myblog.service.IEsBlogService;

@Service
public class BlogServiceImpl implements IBlogService {

	@Autowired
	private BlogRepository blogRepository;

	@Autowired
	private IEsBlogService esBlogService;

	@Transactional
	public Blog savaBlog(Blog blog) {
		boolean isNew = (blog.getId() == null);
		EsBlog esBlog = null;

		Blog returnBlog = blogRepository.save(blog);

		if (isNew) {
			esBlog = new EsBlog(returnBlog);
		} else {
			esBlog = esBlogService.getEsBlogByBlogId(blog.getId());
			esBlog.update(returnBlog);
		}

		esBlogService.updateEsBlog(esBlog);
		return returnBlog;
	}

	@Transactional
	public void removeBlog(Long id) {
		blogRepository.delete(id);
		EsBlog esblog = esBlogService.getEsBlogByBlogId(id);
		esBlogService.removeEsBlog(esblog.getId());
	}

	public Blog getBlogById(Long id) {
		return blogRepository.findOne(id);
	}

	/**
	 * 最新
	 */
	public Page<Blog> listBlogByTitleVote(User user, String title, Pageable pageable) {
		// 模糊查询
		title = "%" + title + "%";
		String tags = title;
		Page<Blog> blogs = blogRepository.findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(title, user,
				tags, user, pageable);
		return blogs;
	}

	/**
	 * 最热排序 逆序
	 */
	@Override
	public Page<Blog> listBlogsByTitleVoteAndSort(User user, String title, Pageable pageable) {
		// 模糊查询
		title = "%" + title + "%";
		Page<Blog> blogs = blogRepository.findByUserAndTitleLike(user, title, pageable);
		return blogs;
	}

	@Override
	public void readingIncrease(Long id) {
		Blog blog = blogRepository.findOne(id);
		blog.setReadSize(blog.getCommentSize() + 1); // 这里看具体业务,我这里是评论+1 也可以是read+1
		this.savaBlog(blog);
	}

	// @Transactional // 应该加上事务
	public Blog createComment(Long blogId, String commentContent) {
		Blog originalBlog = blogRepository.findOne(blogId);
		// 我们虽然没有传user,但是可以通过 SecurityContext 上下文获取认证的用户信息的!!
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Comment comment = new Comment(commentContent, user);
		originalBlog.addComment(comment);
		return this.savaBlog(originalBlog);
	}

	// @Transactional // 应该加上事务
	public void removeComment(Long blogId, Long commentId) {
		Blog originalBlog = blogRepository.findOne(blogId);
		originalBlog.removeComment(commentId);
		this.savaBlog(originalBlog);
	}

	// @Transactional // 应该加上事务
	public Blog createVote(Long blogId) {
		Blog originalBlog = blogRepository.findOne(blogId);
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Vote vote = new Vote(user);
		boolean isExist = originalBlog.addVote(vote);
		if (isExist) {
			throw new IllegalArgumentException("该用户已经点过赞了");
		}
		// return blogRepository.save(originalBlog);
		return this.savaBlog(originalBlog);
	}

	// @Transactional // 应该加上事务
	public void removeVote(Long blogId, Long voteId) {
		Blog originalBlog = blogRepository.findOne(blogId);
		originalBlog.removeVote(voteId);
		// blogRepository.save(originalBlog);
		this.savaBlog(originalBlog);
	}

	@Override
	public Page<Blog> listBlogsByCatalog(Catalog catalog, Pageable pageable) {
		return blogRepository.findByCatalog(catalog, pageable);
	}
}
