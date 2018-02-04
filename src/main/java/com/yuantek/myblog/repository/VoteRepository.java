package com.yuantek.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yuantek.myblog.domain.Vote;
/**
 * 点赞资源库
 * @author mizhe
 *
 */
public interface VoteRepository extends JpaRepository<Vote, Long> {

	
}
