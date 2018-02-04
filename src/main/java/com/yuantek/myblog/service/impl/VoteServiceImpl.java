package com.yuantek.myblog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuantek.myblog.domain.Vote;
import com.yuantek.myblog.repository.VoteRepository;
import com.yuantek.myblog.service.IVoteService;

@Service
public class VoteServiceImpl implements IVoteService {

	@Autowired
	private VoteRepository voteRepository;

	public Vote getVoteById(Long id) {
		return voteRepository.findOne(id);
	}

	public void removeVote(Long id) {
		voteRepository.delete(id);
	}

}
