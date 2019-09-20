package com.chc.peaceworld.user.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chc.peaceworld.common.BaseEntity;
import com.chc.peaceworld.security.entity.Authority;
import com.chc.peaceworld.security.entity.PlatformType;
import com.chc.peaceworld.security.entity.Resource;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Accessors(chain = true)
@TableName("sys_user")
public class User extends BaseEntity {

    @TableField
    private Long id;
    /**
     * 微信ID
     */
    private String openid;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户姓名
     */
    private String firstname;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 用户性别(0-未知,1-男性,2-女性)
     */
    private GenderStatus gender;

    /**
     * 密码
     */
    private String password;

    /**
     * 平台（match-前台 back-后台 wechat-微信）
     */
    private PlatformType platform;

    /**
     * 是否可用
     */
    private Boolean enabled;

    private Date lastPasswordResetDate;


    @TableField(exist = false)
    private List<Authority> authorities;
    @TableField(exist = false)
    private Set<Resource> resources;
}
