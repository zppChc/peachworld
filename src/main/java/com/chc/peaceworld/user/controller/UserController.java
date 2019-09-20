package com.chc.peaceworld.user.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.chc.peaceworld.common.BaseController;
import com.chc.peaceworld.common.JsonResult;
import com.chc.peaceworld.exception.AuthenticationException;
import com.chc.peaceworld.security.JwtTokenUtil;
import com.chc.peaceworld.security.JwtUser;
import com.chc.peaceworld.security.entity.Authority;
import com.chc.peaceworld.security.entity.AuthorityName;
import com.chc.peaceworld.security.entity.PlatformType;
import com.chc.peaceworld.security.entity.Resource;
import com.chc.peaceworld.security.service.IAuthorityService;
import com.chc.peaceworld.security.service.IResourceService;
import com.chc.peaceworld.user.entity.GenderStatus;
import com.chc.peaceworld.user.entity.ParamUser;
import com.chc.peaceworld.user.entity.User;
import com.chc.peaceworld.user.service.IUserService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IResourceService resourceService;
    @Autowired
    private IAuthorityService authorityService;

    @ApiOperation(value = "用户注册", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "form", name = "username", value = "用户名", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "form", name = "firstname", value = "用户姓名", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "form", name = "password", value = "密码", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "form", name = "role", value = "角色", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "form", name = "platform", value = "平台", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "form", name = "gender", value = "性别", required = true, dataType = "string"),
    })
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @Transactional
    public JSONObject register(@RequestParam String username, @RequestParam String firstname, @RequestParam String password, @RequestParam AuthorityName role
            , @RequestParam PlatformType platform, @RequestParam GenderStatus gender) throws AuthenticationException {
        if (userService.getByUsername(username) != null) {
            return JsonResult.fail("用户已存在", JsonResult.ERROR_CODE);
        }
        Integer i = userService.register(null, username, firstname, password, role, platform, null, gender);
        if (i.equals(0)) {
            return JsonResult.fail("用户已存在", JsonResult.ERROR_CODE);
        }
        return JsonResult.success("注册成功");
    }

    @ApiOperation(value = "登录", notes = "")
    @ApiImplicitParams({
    })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JSONObject createAuthenticationToken(@RequestBody ParamUser authenticationRequest) throws AuthenticationException {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        // User user = userService.findByUsername(authenticationRequest.getUsername());
        // Reload password post-security so we can generate the token
        // 此时开始 username为userID
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        // Return the token
        return JsonResult.success(token);
    }

    @ApiOperation(value = "修改密码", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "newPassword", value = "密码", required = true, dataType = "string"),
    })
    @RequestMapping(value = "/modifyPwd", method = RequestMethod.PUT)
    @Transactional
    public JSONObject updatePassword(HttpServletRequest request, @RequestParam String newPassword) {
        User user = getUser(request);
        String password = userService.setPassword(newPassword);
        userService.updatePassword(user.getId(), password);
        return JsonResult.success("成功");
    }

    @ApiOperation(value = "重置密码", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, dataType = "string"),
    })
    @RequestMapping(value = "/resetPwd/{id}", method = RequestMethod.PUT)
    @Transactional
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MANAGER')")
    public JSONObject resetPassword(HttpServletRequest request, @PathVariable Long id) {
        String password = userService.setPassword("huahaoyueyuan123");
        userService.updatePassword(id, password);
        return JsonResult.success("成功");
    }

    @ApiOperation(value = "用户资源", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "token", required = true, dataType = "string"),
    })
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public JSONObject register(HttpServletRequest request) {
        Set<Resource> menuSet = new LinkedHashSet<>();

        // 根据用户ID查出对应的资源
        User user = getUser(request);
        List<Authority> authorities = authorityService.selectByUserId(user.getId());
        authorities.forEach(authority -> {
            List<Resource> resourceList = resourceService.selectByAuthorityIdAndType(authority.getId());
            menuSet.addAll(resourceList);
        });

        for (Resource resource : menuSet) {
            Set<String> buttonNameSet = new HashSet<>();
            authorities.forEach(authority -> {
                List<Resource> buttonList = resourceService.selectByAuthorityIdAndTypeAndMenuId(authority.getId(), resource.getId());
                buttonNameSet.addAll(buttonList.stream().map(Resource::getName).collect(Collectors.toSet()));
            });
            resource.setButtons(buttonNameSet);
        }

        // 改造返回格式
        Set<Resource> resourceSet = resourceService.changeStyle(menuSet);
        user.setResources(resourceSet);
        return JsonResult.success(user);

    }

    @ApiOperation(value = "刷新token", notes = "")
    @ApiImplicitParams({
            //  @ApiImplicitParam(paramType = "header", name = "Authorization", value = "token", required = true, dataType = "string"),
    })
    @RequestMapping(value = "/refresh", method = RequestMethod.GET)
    public JSONObject refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String authToken = request.getHeader(tokenHeader);
        final String token = authToken.substring(7);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        User user = userService.findByUsername(username);
        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(String.valueOf(user.getId()));

        if (jwtTokenUtil.canTokenBeRefreshed(token, jwtUser.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return JsonResult.success(refreshedToken);
        } else {
            return JsonResult.success(null);
        }

    }

    @ApiOperation(value = "修改用户状态", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "token", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "path", name = "id", value = "id", required = true, dataType = "long"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "状态", required = true, dataType = "boolean"),

    })
    @RequestMapping(value = "/enabled/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MANAGER')")
    public JSONObject account(HttpServletRequest request, @PathVariable Long id, @RequestParam Boolean status) {
        boolean b = userService.updateById(new User().setId(id).setEnabled(status));
        return b ? JsonResult.success("禁用成功") : JsonResult.fail("禁用失败", JsonResult.ERROR_CODE);
    }

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ApiOperation(value = "查看所有账户", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "Authorization", value = "token", required = true, dataType = "string"),
            @ApiImplicitParam(name = "page", value = "第几页", dataType = "int", required = false, paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页几条", dataType = "int", required = false, paramType = "query"),
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MANAGER')")
    public JSONObject selectAccount(HttpServletRequest request,
                                    @RequestParam(required = false, defaultValue = "1") Integer page,
                                    @RequestParam(required = false, defaultValue = "10") Integer size) {
        Page<User> pageInfo = new Page<>(page, size);
        IPage<User> userIPage = userService.page(pageInfo, new QueryWrapper<User>().notIn("username", "admin"));
        userIPage.getRecords().forEach(user -> {
            user.setAuthorities(authorityService.selectByUserId(user.getId()));
        });
        return JsonResult.success(userIPage);
    }

    /**
     * Authenticates the user. If something is wrong, an {@link AuthenticationException} will be thrown
     */
    private void authenticate(String username, String password) {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials!", e);
        }
    }
}
