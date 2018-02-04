package com.yuantek.myblog.vo;

import java.io.Serializable;

import com.yuantek.myblog.domain.Catalog;

public class CatalogVo implements Serializable {
	private static final long serialVersionUID = -204986717071762497L;

	private String username;
	private Catalog catalog;

	public CatalogVo() {

	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Catalog getCatalog() {
		return catalog;
	}

	public void setCatalog(Catalog catalog) {
		this.catalog = catalog;
	}

}
