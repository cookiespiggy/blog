package com.yuantek.myblog.controller;

import java.util.List;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yuantek.myblog.domain.Blog;
import com.yuantek.myblog.domain.Comment;
import com.yuantek.myblog.domain.User;
import com.yuantek.myblog.service.IBlogService;
import com.yuantek.myblog.service.ICommentService;
import com.yuantek.myblog.util.ConstraintViolationExceptionHandler;
import com.yuantek.myblog.vo.Response;

/**
 * 评论控制器
 * 
 * @author mi.zhe
 *
 */
@Controller
@RequestMapping("/comments")
public class CommentController {

	@Autowired
	private IBlogService blogService;

	@Autowired
	private ICommentService commentService;
	
	/**
	 * 获取评论列表
	 * @param blogId
	 * @param model
	 * @return
	 */
	@GetMapping
	public String listComments(@RequestParam(value="blogId",required=true) Long blogId,Model model) {
		Blog blog = blogService.getBlogById(blogId);
		List<Comment> comments = blog.getComments();
		
		// 判断操作用户是否是评论的所有者 (所有者有删除按钮)
		String commentOwner = "";
		// SecurityContextHolder在控制层要做非空等校验,在service等可以不做非空校验 ? 上一层校验的话,可以不做校验了
		if(SecurityContextHolder.getContext().getAuthentication() != null 
			&& SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
			&& !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")
				) {
			User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if(principal != null) {
				commentOwner = principal.getUsername();
			}
		}
		
		model.addAttribute("commentOwner", commentOwner);
		model.addAttribute("comments", comments);
		return "userspace/blog :: #mainContainerRepleace";
	}
	
	/**
	 * 发表评论
	 * @param blogId
	 * @param commentContent
	 * @return
	 */
	@PostMapping
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')") // 指定角色权限才能操作此方法 , 游客不能发表评论
	public ResponseEntity<Response> createComment(Long blogId,String commentContent) {
		try {
			blogService.createComment(blogId, commentContent);
		} catch (ConstraintViolationException e)  {
			// 老实讲,这里写的api restful的不够好,以后参考张志君(大牛)的淘淘进行改写
			// 这样的话,异常也返回了ok
			// 所以说代码是一点一点写好的!!!这一点很重要
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		
		return ResponseEntity.ok().body(new Response(true,"处理成功",null));
	}
	
	@DeleteMapping("/{id}")
	// spring的aop功能还是很强大的,一看,此方法原本没有权限这块,一加上此注解,那么就有了权限了
	// 所以此方法被增强了,利用了代理机制
	@PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')") // 指定角色权限才能操作此方法 
	public ResponseEntity<Response> deleteBlog(@PathVariable("id") Long id, Long blogId) {
		boolean isOwner = false;
		User user = commentService.getCommentById(id).getUser();
		
		// 判断操作用户是否是评论的所有者
		if (SecurityContextHolder.getContext().getAuthentication() !=null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				 &&  !SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString().equals("anonymousUser")) {
			User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 
			if (principal !=null && user.getUsername().equals(principal.getUsername())) {
				isOwner = true;
			} 
		} 
		
		if (!isOwner) {
			return ResponseEntity.ok().body(new Response(false, "没有操作权限"));
		}
		try {
			blogService.removeComment(blogId, id);
			commentService.removeComment(id);
		} catch (ConstraintViolationException e)  {
			return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
		} catch (Exception e) {
			return ResponseEntity.ok().body(new Response(false, e.getMessage()));
		}
		
		return ResponseEntity.ok().body(new Response(true, "处理成功", null));
	}
	
}
