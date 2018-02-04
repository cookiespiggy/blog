package com.yuantek.myblog.controller;

import com.google.common.collect.Lists;
import com.yuantek.myblog.domain.Authority;
import com.yuantek.myblog.service.IAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.yuantek.myblog.domain.User;
import com.yuantek.myblog.service.IUserService;

import java.util.List;

/**
 * 主页控制器.
 * 
 * 	https://www.cnblogs.com/softidea/p/5991897.html
 *  当使用 WebSecurityConfigurerAdapter，注销功能将会被自动应用，也就是说，就算不写也有用。
	默认情况下访问/logout将会将用户注销，包含的内容有：
	1、使HttpSession失效
	2、清空已配置的RememberMe验证
	3、清空 SecurityContextHolder
	4、重定向到 /login?logout
	想自定义logout页面,请参考上面的博客
 * @author mi.zhe
 *
 */
@Controller
public class MainController {

	// 用户的权限id,也就是普通博主的用户id,注意不是管理员的
	// 也就是说,注册的用户,通通都是博主
	// 换句话说,管理员是没办法注册的,管理员是通过后台来新增的
	// 普通用户是可以通过注册来生成的
	private static final Long ROLE_USER_AUTHORITY_ID = 2L;
	@Autowired
	private IUserService userService;
	@Autowired
	private IAuthorityService authorityService;

	
	@GetMapping("/")
	public String root() {
		return "redirect:/index";
	}
	@GetMapping("/index")
	public String index() {
		//return "index";
		return "redirect:/blogs";
	}
	
	/**
	 * 获取登录界面
	 * @return
	 */
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/login-error")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		model.addAttribute("errorMsg", "登录失败,用户名或者密码错误!");
		return "login";
	}
	
	@GetMapping("/register")
	public String register() {
		return "register";
	}
	
	@PostMapping("/register")
	public String registerUser(User user) {
		//处理权限
		List<Authority> authorities = Lists.newArrayList();
		authorities.add(authorityService.getAuthorityById(ROLE_USER_AUTHORITY_ID));
		user.setAuthoritys(authorities);

		userService.registerUser(user);
		return "redirect:/login";
	}
}
