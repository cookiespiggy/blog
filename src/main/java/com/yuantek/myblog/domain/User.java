package com.yuantek.myblog.domain;

import java.util.Collection;
import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.Size;

import com.google.common.collect.Lists;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * JPA ====> 用户实体
 * 
 * JPA完成实体和数据库之间的映射,我们只需要关心业务对象之间的关系即可 颠覆开发模式
 * 
 * @author mi.zhe
 *
 */
@Entity // 实体
public class User implements UserDetails {
	private static final long serialVersionUID = -9120603368392621749L;

	@Id // 主键
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 主键生成策略,根据数据库自增
	private Long id;// 实体的唯一标识,任何实现都要有一个唯一的标识

	@NotEmpty(message = "姓名不能为空")
	@Size(min = 2, max = 20)
	@Column(nullable = false, length = 20) // 映射为字段,值不能为空
	private String name;

	@NotEmpty(message = "邮箱不能为空")
	@Size(max = 50)
	@Email(message = "邮箱格式不正确")
	@Column(nullable = false, length = 50, unique = true) // 映射为字段,值不能为空,唯一
	private String email;

	@NotEmpty(message = "账号不能为空")
	@Size(min = 3, max = 20)
	@Column(nullable = false, length = 20, unique = true) // 映射为字段,值不能为空,唯一
	private String username;// 用户账号,用户登录时的唯一标识

	@NotEmpty(message = "密码不能为空")
	@Size(max = 100)
	@Column(length = 100)
	private String password; // 登录时密码
	private String avatar; // 头像图片地址

	/**
	 *   注意此处:
	 *   	建立起用户和权限之间的关系:多对多
	 */
	@ManyToMany(cascade = CascadeType.DETACH,fetch = FetchType.EAGER)
	@JoinTable(name = "user_authority",joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"),
			inverseJoinColumns = @JoinColumn(name = "authority_id",referencedColumnName = "id"))
	private List<Authority> authoritys;
	
	protected User() { // protected 防止直接使用
		super();
	}

	public User(Long id, String name, String email) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
	}

	public User(String name, String email, String username, String password) {
		super();
		this.name = name;
		this.email = email;
		this.username = username;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 加密密码
	 * @param password
	 */
	public void setEncodePassword(String password) {
		//感觉不能这么搞
	}
	
	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return String.format("User ===> [id=%d || username='%s' || name='%s' || email='%s']", id, username, name,
				email);// 格式化
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 需将List<Authority> 转成List<SimpleGrantedAuthority> ,否则前端拿不到角色列表名称
		// SimpleGrantedAuthority 是一个字符串
		List<SimpleGrantedAuthority> simpleGrantedAuthorities = Lists.newArrayList();
		this.authoritys.forEach(ga -> simpleGrantedAuthorities.add(new SimpleGrantedAuthority(ga.getAuthority())));
		return simpleGrantedAuthorities;
	}

	public void setAuthoritys(List<Authority> authoritys) {
		this.authoritys = authoritys;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
