package com.chc.peaceworld.security.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chc.peaceworld.common.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * <p>
 * 资源表
 * </p>
 *
 * @author lite
 * @since 2019-06-18
 */
@Data
@Accessors(chain = true)
@TableName("sys_resource")
public class Resource extends BaseEntity {

    @TableId
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * URL
     */
    private String url;

    /**
     * 资源类型(MENU,BUTTON)
     */
    private String type;

    /**
     * 图标
     */
    private String icon;

    /**
     * 权重
     */
    private Integer weight;

    /**
     * 父资源
     */
    private Long parentId;

    @TableField(exist = false)
    private Set<String> buttons;
    @TableField(exist = false)
    private Set<Resource> children;


}
