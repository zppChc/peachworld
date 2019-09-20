package com.chc.peaceworld.common;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseEntity implements Serializable {
    @ApiModelProperty(hidden = true)
    protected Date createdAt;

    @ApiModelProperty(hidden = true)
    protected Date updatedAt;
}
