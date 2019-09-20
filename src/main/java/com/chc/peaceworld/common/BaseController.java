package com.chc.peaceworld.common;

import com.chc.peaceworld.security.JwtTokenUtil;
import com.chc.peaceworld.user.entity.User;
import com.chc.peaceworld.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpServletRequest;

public class BaseController {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public String getUsername(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader).substring(7);

        return jwtTokenUtil.getUsernameFromToken(token);
    }

    public User getUser(HttpServletRequest request) {
        String username = this.getUsername(request);
        return userService.getByUsername(username);
    }

    public ModelMap getModelMap(String status, Object data, String msg){
        ModelMap modelMap=new ModelMap();
        modelMap.put("status", status);
        modelMap.put("data", data);
        modelMap.put("msg", msg);
        return modelMap;

    }
}
