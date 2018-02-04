package com.yuantek.myblog.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuantek.myblog.domain.Catalog;
import com.yuantek.myblog.domain.User;
import com.yuantek.myblog.repository.CatalogRepository;
import com.yuantek.myblog.service.ICatalogService;

@Service
public class CatalogServiceImpl implements ICatalogService {

	@Autowired
	private CatalogRepository catalogRepository;

	public Catalog saveCatalog(Catalog catalog) {
		// 判断重复
		List<Catalog> list = catalogRepository.findByUserAndName(catalog.getUser(), catalog.getName());
		if (list != null && list.size() > 0) {
			throw new IllegalArgumentException("该分类已经存在了");
		}
		return catalogRepository.save(catalog);
	}

	public void removeCatalog(Long id) {
		catalogRepository.delete(id);
	}

	public Catalog getCatalogById(Long id) {
		return catalogRepository.findOne(id);
	}

	public List<Catalog> listCatalogs(User user) {
		return catalogRepository.findByUser(user);
	}

}
