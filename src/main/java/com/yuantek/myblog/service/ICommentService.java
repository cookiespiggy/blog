package com.yuantek.myblog.service;

import com.yuantek.myblog.domain.Comment;

/**
 * 	评论服务接口
 * @author mi.zhe
 *
 */
public interface ICommentService {

	/**
	 * 根据id获取评论
	 * @param id
	 * @return
	 */
	Comment getCommentById(Long id);
	
	/**
	 * 删除评论(默认都是根据id删除)
	 * @param id
	 */
	void removeComment(Long id);
}
