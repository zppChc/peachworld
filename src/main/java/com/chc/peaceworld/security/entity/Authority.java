package com.chc.peaceworld.security.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.chc.peaceworld.common.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@TableName("sys_authority")
public class Authority extends BaseEntity {
    @TableId
    private Long id;
    // private AuthorityName name;
    private String name;
}
