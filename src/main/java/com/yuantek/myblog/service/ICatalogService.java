package com.yuantek.myblog.service;

import java.util.List;

import com.yuantek.myblog.domain.Catalog;
import com.yuantek.myblog.domain.User;

/**
 * 分类服务接口
 * 
 * @author mizhe
 *
 */
public interface ICatalogService {

	/**
	 * 保存分类
	 * 
	 * @param catalog
	 * @return
	 */
	Catalog saveCatalog(Catalog catalog);

	/**
	 * 删除分类
	 * 
	 * @param id
	 */
	void removeCatalog(Long id);

	/**
	 * 根据id获取分类
	 * 
	 * @param id
	 * @return
	 */
	Catalog getCatalogById(Long id);

	/**
	 * 获取个人的分类列表, 不需要分页,因为数据不会很多,把查询出来的列表通通给界面即可
	 * 
	 * @param user
	 * @return
	 */
	List<Catalog> listCatalogs(User user);
}
