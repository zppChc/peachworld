package com.chc.peaceworld.user.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ParamUser {
    private String username;
    private String password;
}
