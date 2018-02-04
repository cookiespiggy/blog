package com.yuantek.myblog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yuantek.myblog.domain.Catalog;
import com.yuantek.myblog.domain.User;

/**
 * 分类资源库
 * 
 * @author mizhe
 *
 */
public interface CatalogRepository extends JpaRepository<Catalog, Long> {

	/**
	 * 通过用户查询分类列表
	 * 
	 * @param user
	 * @return
	 */
	List<Catalog> findByUser(User user);

	/**
	 * 通过用户和分类名称查询分类列表
	 * 
	 * @param user
	 * @param name
	 * @return
	 */
	List<Catalog> findByUserAndName(User user, String name);
}
