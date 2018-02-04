package com.yuantek.myblog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yuantek.myblog.domain.Comment;
/**
 * 评论资源库
 * @author mi.zhe
 *
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {

}
