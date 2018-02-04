package com.yuantek.myblog.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

/**
 * JPA ====> 权限实体
 * 
 * @author mi.zhe
 *
 */
@Entity
public class Authority implements GrantedAuthority {

	private static final long serialVersionUID = 2424353741929010566L;

	@Id // 主键
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 权限id

	@Column(nullable = false) // 非空
	private String name; // 权限名称

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// public String getName() {
	// return name;
	// }

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getAuthority() { // 替代getName()
		return this.name;
	}

}
