package com.yuantek.myblog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yuantek.myblog.domain.Comment;
import com.yuantek.myblog.repository.CommentRepository;
import com.yuantek.myblog.service.ICommentService;

@Service
public class CommentServiceImpl implements ICommentService {

	@Autowired
	private CommentRepository commentRepository;

	public Comment getCommentById(Long id) {
		return commentRepository.findOne(id);
	}

	public void removeComment(Long id) {
		commentRepository.delete(id);
	}

}
