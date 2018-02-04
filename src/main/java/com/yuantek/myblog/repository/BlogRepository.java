package com.yuantek.myblog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.yuantek.myblog.domain.Blog;
import com.yuantek.myblog.domain.Catalog;
import com.yuantek.myblog.domain.User;

/**
 * 博客资源库
 */
public interface BlogRepository extends JpaRepository<Blog, Long> {
	/**
	 * 根据用户名和博客标题(模糊查询)分页 查询博客列表 最热
	 */
	Page<Blog> findByUserAndTitleLike(User user, String title, Pageable pageable);

	/**
	 * 根据用户名 博客查询博客列表(时间逆序) 最新
	 */
	Page<Blog> findByTitleLikeAndUserOrTagsLikeAndUserOrderByCreateTimeDesc(String title, User user, String tags,
			User user2, Pageable pageable);

	// 增加分类模块之后,修改BlogRepository
	/**
	 * 根据分类查询博客列表 需求:一个分类下,可以有很多博客
	 * 
	 * @param catalog
	 * @param pageable
	 * @return
	 */
	Page<Blog> findByCatalog(Catalog catalog, Pageable pageable);
}
