package com.yuantek.myblog.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yuantek.myblog.domain.Catalog;
import com.yuantek.myblog.domain.User;
import com.yuantek.myblog.service.ICatalogService;
import com.yuantek.myblog.util.ConstraintViolationExceptionHandler;
import com.yuantek.myblog.vo.CatalogVo;
import com.yuantek.myblog.vo.Response;

/**
 * 分类控制器
 * 
 * @author mizhe
 *
 */
@Controller
@RequestMapping("/catalogs")
public class CatalogController {

	@Autowired
	private ICatalogService catalogService;

	@Autowired
	private UserDetailsService userDetailsService;

	/**
	 * 获取(个人)分类列表
	 * 
	 * @param username
	 * @param model
	 * @return
	 */
	@GetMapping
	public String listComments(@RequestParam(value = "username", required = true) String username, Model model) {
		User user = (User) userDetailsService.loadUserByUsername(username);
		List<Catalog> catalogs = catalogService.listCatalogs(user);

		// 判断操作用户是否是分类的所有者,用来显示是否要显示操作按钮(+ - edit)
		boolean isOwner = false;

		if (SecurityContextHolder.getContext().getAuthentication() != null
				&& SecurityContextHolder.getContext().getAuthentication().isAuthenticated() && !SecurityContextHolder
						.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal != null && user.getUsername().equals(principal.getUsername())) {
				isOwner = true;
			}
		}
		model.addAttribute("isCatalogsOwner", isOwner);
		model.addAttribute("catalogs", catalogs);
		return "userspace/u :: #catalogRepleace";
	}

	/**
	 * 创建分类
	 * 
	 * @param catalogVo
	 * @return
	 */
	@PostMapping
	@PreAuthorize("authentication.name.equals(#catalogVo.username)") // 指定角色权限才能操作此方法
	public ResponseEntity<Response> create(@RequestBody CatalogVo catalogVo) {
		String username = catalogVo.getUsername();
		Catalog catalog = catalogVo.getCatalog();

		User user = (User) userDetailsService.loadUserByUsername(username);

		try {
			catalog.setUser(user);
			catalogService.saveCatalog(catalog);
		} catch (ConstraintViolationException e) {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}

		return ResponseEntity.ok().body(new Response(true, "处理成功", null));
	}

	/**
	 * 删除分类
	 * 
	 * @param username
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	@PreAuthorize("authentication.name.equals(#username)") // 指定角色权限才能操作此方法
	public ResponseEntity<Response> delete(String username, @PathVariable("id") Long id) {

		try {
			catalogService.removeCatalog(id);
		} catch (ConstraintViolationException e) {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		return ResponseEntity.ok().body(new Response(true, "处理成功", null));
	}

	/**
	 * 获取分类编辑界面
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/edit")
	public String getCatalogEdit(Model model) {
		Catalog Catalog = new Catalog(null, null);
		model.addAttribute("catalog", Catalog);
		return "userspace/catalogedit";
	}

	/**
	 * 根据 Id 获取分类编辑界面
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@GetMapping("/edit/{id}")
	public String getCatalogById(@PathVariable("id") Long id, Model model) {
		Catalog catalog = catalogService.getCatalogById(id);
		model.addAttribute("catalog", catalog);
		return "userspace/catalogedit";
	}
}
