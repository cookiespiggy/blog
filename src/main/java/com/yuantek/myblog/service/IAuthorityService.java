package com.yuantek.myblog.service;

import com.yuantek.myblog.domain.Authority;

/**
 * Authority 服务接口
 */
public interface IAuthorityService {

    /**
     * 根据id查询权限
     * @param id
     * @return
     */
    Authority getAuthorityById(Long id);
}
