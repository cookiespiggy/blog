package com.yuantek.myblog.service;

import com.yuantek.myblog.domain.Vote;

/**
 * 点赞服务接口
 * 
 * @author mizhe
 *
 */
public interface IVoteService {

	/**
	 * 根据id获取点赞实体
	 * @param id
	 * @return
	 */
	Vote getVoteById(Long id);
	
	/**
	 * 删除点赞(取消点赞)
	 * @param id
	 */
	void removeVote(Long id);
}
