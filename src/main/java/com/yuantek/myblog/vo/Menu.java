package com.yuantek.myblog.vo;

import java.io.Serializable;

/**
 * 菜单
 * 
 * @author mi.zhe
 *
 */
public class Menu implements Serializable {

	private static final long serialVersionUID = -8903112850972921723L;

	private String name;
	private String url;

	public Menu(String name, String url) {
		super();
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
