package com.yuantek.myblog.service.impl;

import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.yuantek.myblog.domain.User;
import com.yuantek.myblog.repository.UserRepository;
import com.yuantek.myblog.service.IUserService;

@Service
public class UserServiceImpl implements IUserService, UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Transactional
	public User saveOrUpdateUser(User user) {
		return userRepository.save(user);
	}

	@Transactional
	public User registerUser(User user) {
		return userRepository.save(user);
		//throw new RuntimeException("test rex==============");
	}

	@Transactional
	public void deleteUser(Long id) {
		userRepository.delete(id);
	}

	public User getUserById(Long id) {
		return userRepository.findOne(id);
	}

	public Page<User> listUsersByNameLike(String name, Pageable pageable) {
		// 模糊查询
		name = "%" + name + "%";
		return userRepository.findByNameLike(name, pageable);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username);
	}

	@Override
	public List<User> listUsersByUsernames(Collection<String> usernames) {
		return userRepository.findByUsernameIn(usernames);
	}
}
