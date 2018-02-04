package com.yuantek.myblog.vo;

import java.io.Serializable;

/**
 * 标签值对象 因为没有定于标签实体 但是首页需要展示热门标签
 * 
 * @author mizhe
 *
 */
public class TagVo implements Serializable {
	private static final long serialVersionUID = 5565619124351633555L;

	private String name; // 标签名
	private Long count; // 标签数量

	public TagVo(String name, Long count) {
		super();
		this.name = name;
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

}
