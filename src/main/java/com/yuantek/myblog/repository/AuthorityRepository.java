package com.yuantek.myblog.repository;

import com.yuantek.myblog.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 权限资源库
 */
public interface AuthorityRepository extends JpaRepository<Authority,Long> {
}
