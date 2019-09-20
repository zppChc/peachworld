package com.chc.peaceworld.user.entity;

import com.baomidou.mybatisplus.core.enums.IEnum;
import lombok.Getter;

@Getter
public enum GenderStatus implements IEnum<Integer> {
    unknown(0, "未知"),
    boy(1, "男"),
    girl(2, "女");

    private int code;
    private String status;

    GenderStatus(int code, String status) {
        this.code = code;
        this.status = status;
    }


    @Override
    public Integer getValue() {
        return this.code;
    }
}
