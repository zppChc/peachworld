package com.chc.peaceworld.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.chc.peaceworld.security.entity.AuthorityName;
import com.chc.peaceworld.security.entity.PlatformType;
import com.chc.peaceworld.user.entity.GenderStatus;
import com.chc.peaceworld.user.entity.User;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author lite
 * @since 2019-06-17
 */
public interface IUserService extends IService<User> {
    User findByUsername(String username);

    User getByUsername(String username);

    Integer register(String openid, String username, String firstname, String password, AuthorityName role, PlatformType platform, String avatarUrl, GenderStatus genderStatus);

    String setPassword(String newPassword);

    void updatePassword(Long id, String password);

    User selectByOpenid(String openid);

}
