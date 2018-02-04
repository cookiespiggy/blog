package com.yuantek.myblog.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.yuantek.myblog.domain.User;

/**
 * user服务接口,定义了对用户的常用操作
 * 
 * @author mi.zhe
 *
 */
public interface IUserService {

	/**
	 * 新增 || 编辑 用户
	 * 
	 * @param user
	 * @return
	 */
	User saveOrUpdateUser(User user);

	/**
	 * 注册用户
	 */
	User registerUser(User user);

	/**
	 * 删除用户,一般是通过id
	 */
	void deleteUser(Long id);

	/**
	 * 根据id获取用户
	 */
	User getUserById(Long id);

	/**
	 * 根据用户姓名进行[分页 在spring项目中推荐用page]模糊查询
	 */
	Page<User> listUsersByNameLike(String name, Pageable pageable);

	/**
	 * 根据用户名集合查询用户详细信息列表
	 * 
	 * @param usernames
	 * @return
	 */
	List<User> listUsersByUsernames(Collection<String> usernames);
}
