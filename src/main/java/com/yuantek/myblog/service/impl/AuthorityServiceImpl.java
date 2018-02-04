package com.yuantek.myblog.service.impl;

import com.yuantek.myblog.domain.Authority;
import com.yuantek.myblog.repository.AuthorityRepository;
import com.yuantek.myblog.service.IAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl implements IAuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Authority getAuthorityById(Long id) {
        return authorityRepository.findOne(id);
    }
}
