package com.chc.peaceworld.security.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chc.peaceworld.security.entity.Authority;
import com.chc.peaceworld.security.repository.AuthorityMapper;
import com.chc.peaceworld.security.service.IAuthorityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityServiceImpl extends ServiceImpl<AuthorityMapper, Authority> implements IAuthorityService {

    public List<Authority> selectByUserId(Long userId){
        return baseMapper.selectAuthorityList(userId);
    }

    @Override
    public void insert(Long userId, Long authorityId) {
        baseMapper.save(userId,authorityId);
    }
}
