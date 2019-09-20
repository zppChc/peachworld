package com.chc.peaceworld.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chc.peaceworld.common.TimeProvider;
import com.chc.peaceworld.security.entity.Authority;
import com.chc.peaceworld.security.entity.AuthorityName;
import com.chc.peaceworld.security.entity.PlatformType;
import com.chc.peaceworld.security.service.IAuthorityService;
import com.chc.peaceworld.user.entity.GenderStatus;
import com.chc.peaceworld.user.entity.User;
import com.chc.peaceworld.user.repository.UserMapper;
import com.chc.peaceworld.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author lite
 * @since 2019-06-17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private IAuthorityService authorityService;

    @Override
    public User findByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = baseMapper.selectOne(queryWrapper);
        user.setAuthorities(authorityService.selectByUserId(user.getId()));
        return user;
    }

    @Override
    public User getByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        User user = baseMapper.selectOne(queryWrapper);
        return user;
    }

    @Override
    public Integer register(String openid, String username, String firstname, String password, AuthorityName role, PlatformType platform, String avatarUrl, GenderStatus genderStatus) {
        User userToAdd = caseUp(null, null, username, firstname, password, null, null, genderStatus, platform, true,
                new TimeProvider().now());
        if (userToAdd == null) {
            return -1;
        }
        String encode = setPassword(password);
        // final String rawPassword = userToAdd.getPassword();
        userToAdd.setPassword(encode);
        // userToAdd.setRoles(asList("ROLE_USER"));

        // userToAdd.setRoles("ROLE_USER");
        baseMapper.insert(userToAdd);
        Authority authority = authorityService.getOne(new QueryWrapper<Authority>().eq("name", role.toString()));
        authorityService.insert(userToAdd.getId(), authority.getId());

        return 1;
    }

    private User caseUp(Long id, String openid, String username, String firstname, String password, String mobile, String avatarUrl, GenderStatus genderStatus,
                        PlatformType platformType, Boolean isEnabled, Date lastPasswordResetDate) {
        User user = new User();
        user.setId(id);
        user.setOpenid(openid);
        user.setUsername(username);
        user.setFirstname(firstname);
        user.setMobile(mobile);
        user.setAvatarUrl(avatarUrl);
        user.setGender(genderStatus);
        user.setPassword(password);
        user.setPlatform(platformType);
        user.setEnabled(isEnabled);
        user.setLastPasswordResetDate(lastPasswordResetDate);
        return user;
    }

    @Override
    public String setPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    @Override
    public void updatePassword(Long id, String newPassword) {
        baseMapper.updateById(new User().setId(id).setPassword(newPassword));

    }

    @Override
    public User selectByOpenid(String openid) {
        return baseMapper.selectOne(new QueryWrapper<>(new User().setOpenid(openid)));
    }
}
