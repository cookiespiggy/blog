package com.yuantek.myblog.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.yuantek.myblog.domain.Blog;
import com.yuantek.myblog.domain.Catalog;
import com.yuantek.myblog.domain.User;
import com.yuantek.myblog.domain.Vote;
import com.yuantek.myblog.service.IBlogService;
import com.yuantek.myblog.service.ICatalogService;
import com.yuantek.myblog.service.IUserService;
import com.yuantek.myblog.util.ConstraintViolationExceptionHandler;
import com.yuantek.myblog.vo.Response;

/**
 * 用户主页控制器
 * 
 * @author mi.zhe
 *
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {

	@Autowired
	private IUserService userService;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private IBlogService blogService;

	@Autowired
	private ICatalogService catalogService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${file.server.url:http://localhost:8081/upload}")
	private String fileServerUrl;

	/**
	 * 用户主页
	 * 
	 * @param username
	 * @return
	 */
	@GetMapping("/{username}")
	public String userSpace(@PathVariable("username") String username, Model model) {
		User user = (User) userDetailsService.loadUserByUsername(username);
		// 个人资料绑定到模型里面
		model.addAttribute("user", user);
		// 可以出现中文 解决传回的url有中文乱码现象 **** 如果是windows系统开发,就用username.getBytes("gbk")
		// 也可以用springboot的全局变量来接收
		try {
			username = new String(username.getBytes("UTF-8"), "iso8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "redirect:/u/" + username + "/blogs";
	}

	/**
	 * 获取个人设置页面
	 * 
	 * @param username
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/profile")
	@PreAuthorize("authentication.name.equals(#username)") // 用户只能修改自己的profile 防止越权
	public ModelAndView profile(@PathVariable("username") String username, Model model) {
		User user = (User) userDetailsService.loadUserByUsername(username);
		model.addAttribute("user", user);
		model.addAttribute("fileServerUrl", fileServerUrl);// 文件服务器的地址返回给客户端
		return new ModelAndView("userspace/profile", "userModel", model);
	}

	/**
	 * 保存个人设置
	 * 
	 * @param username
	 * @param user
	 * @return
	 */
	@PostMapping("/{username}/profile")
	@PreAuthorize("authentication.name.equals(#username)")
	public String saveProfile(@PathVariable("username") String username, User user) {
		User originalUser = userService.getUserById(user.getId());
		originalUser.setEmail(user.getEmail());
		originalUser.setName(user.getName());

		// 判断密码是否做了变更
		String rawPassword = originalUser.getPassword();
		String encodePassword = passwordEncoder.encode(user.getPassword());
		boolean isMatch = passwordEncoder.matches(rawPassword, encodePassword);

		if (!isMatch) {
			originalUser.setPassword(passwordEncoder.encode(user.getPassword()));
		}

		userService.saveOrUpdateUser(originalUser);
		return "redirect:/u/" + username + "/profile";
	}

	/**
	 * 获取编辑头像的界面
	 * 
	 * @param username
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/avatar")
	@PreAuthorize("authentication.name.equals(#username)")
	public ModelAndView avatar(@PathVariable("username") String username, Model model) {
		User user = (User) userDetailsService.loadUserByUsername(username);
		model.addAttribute("user", user);
		return new ModelAndView("userspace/avatar", "userModel", model);
	}

	/**
	 * 保存头像
	 * 
	 * @param username
	 * @param user
	 * @return
	 */
	@PostMapping("{username}/avatar")
	@PreAuthorize("authentication.name.equals(#username)")
	public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username, @RequestBody User user) {
		String avatarUrl = user.getAvatar();

		User originalUser = userService.getUserById(user.getId());
		originalUser.setAvatar(avatarUrl);
		userService.saveOrUpdateUser(originalUser);

		return ResponseEntity.ok().body(new Response(true, "处理成功", avatarUrl));
	}

	/**
	 * 获取用户的博客列表
	 * 
	 * @param username
	 * @param order
	 * @param catalogId
	 * @param keyword
	 * @param async
	 * @param pageIndex
	 * @param pageSize
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/blogs")
	public String listBlogsByOrder(@PathVariable("username") String username,
			@RequestParam(value = "order", required = false, defaultValue = "new") String order,
			@RequestParam(value = "catalog", required = false) Long catalogId,
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "async", required = false) boolean async,
			@RequestParam(value = "pageIndex", required = false, defaultValue = "0") int pageIndex,
			@RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize, Model model) {

		User user = (User) userDetailsService.loadUserByUsername(username);
		Page<Blog> page = null;
		if (null != catalogId && catalogId > 0) { // 分类查询
			Catalog catalog = catalogService.getCatalogById(catalogId);
			Pageable pageable = new PageRequest(pageIndex, pageSize);
			page = blogService.listBlogsByCatalog(catalog, pageable);
			order = "";
		} else if (order.equals("hot")) { // 最热查询 逆序
			Sort sort = new Sort(Direction.DESC, "readSize", "commentSize", "voteSize");
			Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
			// 最热查询接口
			page = blogService.listBlogsByTitleVoteAndSort(user, keyword, pageable);
		} else if (order.equals("new")) { // 最新查询
			Pageable pageable = new PageRequest(pageIndex, pageSize);
			page = blogService.listBlogByTitleVote(user, keyword, pageable);
		}

		List<Blog> list = page.getContent(); // 当前所在页面数据列表
		model.addAttribute("user", user);
		model.addAttribute("order", order);
		model.addAttribute("catalogId", catalogId);
		model.addAttribute("keyword", keyword);
		model.addAttribute("page", page);
		model.addAttribute("blogList", list);
		// /userspace/u  前面不能加/ 否则java-jar 会找不到模板
		return (async == true ? "userspace/u :: #mainContainerRepleace" : "userspace/u");
	}

	/**
	 * 获取博客展示界面
	 * 
	 * @param username
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/blogs/{id}")
	public String getBlogById(@PathVariable("username") String username, @PathVariable("id") Long id, Model model) {
		User principal = null;
		Blog blog = blogService.getBlogById(id);

		// 每次读取，简单的可以认为阅读量增加1次
		blogService.readingIncrease(id);// update blog set comment_size=?, content=?, create_time=?, html_content=?,
										// read_size=?, summary=?, tags=?, title=?, user_id=?, vote_size=? where id=?

		// 判断操作用户是否是博客的所有者
		boolean isBlogOwner = false;
		if (SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().isAuthenticated() && !SecurityContextHolder
						.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {

			principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal != null && username.equals(principal.getUsername())) {
				isBlogOwner = true;
			}
		}

		// 判断操作用户的点赞情况,如果没点赞,给一个点赞按钮,如果点赞了,给一个取消点赞的按钮
		List<Vote> votes = blog.getVotes();
		Vote currentVote = null; // 当前用户的点赞情况

		if (principal != null) {
			for (Vote vote : votes) {
				if(vote.getUser().getUsername().equals(principal.getUsername())){
					currentVote = vote;
					break;
				}
			}
		}
		model.addAttribute("currentVote", currentVote);

		// 博客是否可编辑
		model.addAttribute("isBlogOwner", isBlogOwner);
		model.addAttribute("blogModel", blog);

		return "userspace/blog";
	}

	/**
	 * 获取新增博客的界面
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/blogs/edit")
	public ModelAndView createBlog(@PathVariable("username") String username, Model model) {
		// 可以在页面选择属于哪一分类
		User user = (User) userDetailsService.loadUserByUsername(username);
		List<Catalog> catalogs = catalogService.listCatalogs(user);
		model.addAttribute("catalogs", catalogs);
		model.addAttribute("blog", new Blog(null, null, null));
		// 图片需求
		model.addAttribute("fileServerUrl", fileServerUrl); // 文件服务器的地址返回给客户端
		return new ModelAndView("userspace/blogedit", "blogModel", model);
	}

	/**
	 * 获取编辑博客的界面
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/{username}/blogs/edit/{id}")
	public ModelAndView editBlog(@PathVariable("username") String username, @PathVariable("id") Long id, Model model) {
		// 可以在页面选择属于哪一分类
		User user = (User) userDetailsService.loadUserByUsername(username);
		List<Catalog> catalogs = catalogService.listCatalogs(user);
		model.addAttribute("catalogs", catalogs);
		model.addAttribute("blog", blogService.getBlogById(id));
		model.addAttribute("fileServerUrl", fileServerUrl); // 文件服务器的地址返回给客户端
		return new ModelAndView("userspace/blogedit", "blogModel", model);
	}

	/**
	 * 保存博客
	 * 
	 * @param username
	 * @param blog
	 * @return
	 */
	@PostMapping("/{username}/blogs/edit")
	@PreAuthorize("authentication.name.equals(#username)")
	public ResponseEntity<Response> saveBlog(@PathVariable("username") String username, @RequestBody Blog blog) {
		// 对分类进行非空校验处理
		if (blog.getCatalog().getId() == null) {
			return ResponseEntity.ok().body(new Response(false, "未选择分类"));
		}

		try {
			// 判断修改还是新增
			if (blog.getId() != null) { // update
				Blog originalBlog = blogService.getBlogById(blog.getId());
				originalBlog.setTitle(blog.getTitle());
				originalBlog.setContent(blog.getContent());
				originalBlog.setSummary(blog.getSummary());
				// 保存分类
				originalBlog.setCatalog(blog.getCatalog());
				// 保存标签
				originalBlog.setTags(blog.getTags());
				blogService.savaBlog(originalBlog);
			} else { // new save
				User user = (User) userDetailsService.loadUserByUsername(username);
				blog.setUser(user);
				blogService.savaBlog(blog);
			}
		} catch (ConstraintViolationException e) {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}

		String redirectUrl = "/u/" + username + "/blogs/" + blog.getId();
		return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
	}

	/**
	 * 删除博客
	 * 
	 * @param username
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{username}/blogs/{id}")
	@PreAuthorize("authentication.name.equals(#username)")
	public ResponseEntity<Response> deleteBlog(@PathVariable("username") String username, @PathVariable("id") Long id) {

		try {
			blogService.removeBlog(id);
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}

		String redirectUrl = "/u/" + username + "/blogs";
		return ResponseEntity.ok().body(new Response(true, "处理成功", redirectUrl));
	}
}
