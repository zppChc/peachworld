package com.chc.peaceworld.security.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chc.peaceworld.security.entity.Authority;

import java.util.List;

public interface IAuthorityService extends IService<Authority> {

     List<Authority> selectByUserId(Long userId);

    void insert(Long userId, Long authorityId);

}
