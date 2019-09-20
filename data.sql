drop database if exists `peaceworld`;
create database `peaceworld`;
use `peaceworld`;

drop table if exists `hello`;
create table hello
(
    id         bigint auto_increment
        primary key,
    name       varchar(20) default ''                not null comment '用户名',
    age        int         default 0                 not null comment '年龄',
    created_at timestamp   default CURRENT_TIMESTAMP not null,
    updated_at timestamp   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
) comment 'hello表' collate = utf8mb4_unicode_ci;

drop table if exists sys_authority;
create table sys_authority
(
    id         int auto_increment primary key,
    name       varchar(50) default ''                not null comment '角色名称',
    created_at timestamp   default CURRENT_TIMESTAMP not null,
    updated_at timestamp   default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
) comment '角色表' collate = utf8mb4_unicode_ci;
insert into sys_authority (id, name)
values (1, 'ROLE_USER'), /* 用户 */
       (2, 'ROLE_ARTICLE_EDITOR'), /* 文章编辑 */
       (3, 'ROLE_MANAGER'), /* 高级管理员 */
       (4, 'ROLE_ADMIN'), /* 终极管理员 */
       (5, 'ROLE_COMMON_MANAGER'); /* 普通管理员 */

drop table if exists sys_user_authority;
create table sys_user_authority
(
    user_id      bigint(11) default 0 not null comment '用户ID',
    authority_id int        default 0 not null comment '角色ID',
    UNIQUE KEY `sys_user_authority_user_id_authority_id` (`user_id`, `authority_id`)
) comment '用户_角色表' collate = utf8mb4_unicode_ci;

drop table if exists `sys_resource`;
create table `sys_resource`
(
    `id`         bigint(11) auto_increment primary key,
    `name`       varchar(255) not null default '' comment '名称',
    `url`        varchar(255) not null default '' comment 'URL',
    `type`       varchar(255) not null default '' comment '资源类型(MENU,BUTTON)',
    `icon`       varchar(50)  not null default '' comment '图标',
    `weight`     int          not null default 0 comment '权重',
    `parent_id`  bigint(11)   not null default 0 comment '父资源',
    `created_at` timestamp             default CURRENT_TIMESTAMP not null,
    `updated_at` timestamp             default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
) ENGINE = InnoDB
  collate = utf8mb4_unicode_ci comment '资源表';

drop table if exists `sys_authority_resource`;
create table `sys_authority_resource`
(
    `authority_id` bigint(11) not null default 0 comment '角色ID',
    `resource_id`  bigint(11) not null default 0 comment '资源ID',
    UNIQUE KEY `sys_authority_resource_authority_id_resource_id` (`authority_id`, `resource_id`)
) ENGINE = InnoDB
  collate = utf8mb4_unicode_ci comment '用户资源表';

drop table if exists sys_user;
create table sys_user
(
    `id`                       bigint(11) auto_increment primary key,
    `openid`                   varchar(100)        default 0                     NOT NULL COMMENT '微信ID',
    `username`                 varchar(255) BINARY default ''                    NOT NULL COMMENT '用户昵称',
    `firstname`                varchar(11)         default ''                    NOT NULL COMMENT '用户姓名',
    `mobile`                   varchar(11)         default ''                    NOT NULL COMMENT '手机号',
    `avatar_url`               varchar(255)        default ''                    NOT NULL COMMENT '用户头像',
    `gender`                   int                 default 0                     NOT NULL COMMENT '用户性别(0-未知,1-男性,2-女性)',
    `password`                 VARCHAR(100)        default ''                    NOT NULL COMMENT '密码',
    `platform`                 varchar(255)        default ''                    NOT NULL COMMENT '平台（match-前台 back-后台 wechat-微信）',
    `enabled`                  tinyint(1)          DEFAULT '1'                   NOT NULL COMMENT '是否可用',
    `last_password_reset_date` timestamp           default '0000-00-00 00:00:00' NOT NULL COMMENT '',
    `created_at`               timestamp           default CURRENT_TIMESTAMP     NOT NULL,
    `updated_at`               timestamp           default CURRENT_TIMESTAMP     NOT NULL on update CURRENT_TIMESTAMP
) comment '用户表' collate = utf8mb4_unicode_ci;






