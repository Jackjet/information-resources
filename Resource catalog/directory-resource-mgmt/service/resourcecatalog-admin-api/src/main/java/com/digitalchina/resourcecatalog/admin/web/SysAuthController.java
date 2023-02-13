package com.digitalchina.resourcecatalog.admin.web;

//import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.digitalchina.resourcecatalog.admin.service.KeycloakTokenService;
import com.digitalchina.resourcecatalog.admin.service.LogHelper;
import com.digitalchina.resourcecatalog.admin.util.Permission;
import com.digitalchina.resourcecatalog.admin.util.PermissionUtil;
import com.digitalchina.resourcecatalog.admin.util.UserInfo;
import com.digitalchina.resourcecatalog.core.util.IpUtil;
import com.digitalchina.resourcecatalog.core.util.JacksonUtil;
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.core.util.bcrypt.BCryptPasswordEncoder;
import com.digitalchina.resourcecatalog.db.domain.SysPermission;
import com.digitalchina.resourcecatalog.db.domain.SysRole;
import com.digitalchina.resourcecatalog.db.domain.SysUser;
import com.digitalchina.resourcecatalog.db.service.SysPermissionService;
import com.digitalchina.resourcecatalog.db.service.SysRoleService;
import com.digitalchina.resourcecatalog.db.service.SysUserService;
//import d1.framework.authz.configuration.AuthzConfig;
//import d1.framework.authz.service.AuthzMyInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
//import org.apache.commons.beanutils.ConvertUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.digitalchina.resourcecatalog.admin.util.AdminResponseCode.ADMIN_INVALID_ACCOUNT;
import static com.digitalchina.resourcecatalog.admin.util.AdminResponseCode.KEYCLOAK_USER_NO_EXIST;

/**
 * @author: create by wangmh
 * @name: SysAuthController.java
 * @description:
 * @date:2020/4/30
 **/
@RestController
@RequestMapping("/admin/auth")
@Validated
@Api(value = "认证接口", tags = "认证接口")
public class SysAuthController {

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysPermissionService permissionService;

    @Autowired
    private LogHelper logHelper;

    @Value("${resourcecatalog.portalUrl}")
    private String portalUrl;

    @Autowired
    KeycloakTokenService keycloakTokenService;

    @Value("${resourcecatalog.user.init.password}")
    private String initPwd;


//    @Autowired
//    private AuthzMyInfoService authzMyInfoService;

    /*
     *  { username : value, password : value }
     */
    @PostMapping("/login")
    @ApiOperation("登录")
    public Object login(@RequestBody String body,HttpServletRequest request) {
//        String username = JacksonUtil.parseString(body, "username");
//        String password = JacksonUtil.parseString(body, "password");

//        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
//            return ResponseUtil.badArgument();
//        }
        String access_token = JacksonUtil.parseString(body, "access_token");
        if (StringUtils.isEmpty(access_token)) {
            return ResponseUtil.badArgument();
        }
        String username = keycloakTokenService.getUserNameByToken("Bearer " + access_token);

        if (StringUtils.isEmpty(username)) {
            return ResponseUtil.badArgument();
        }
        SysUser sysUser =userService.findByIdOrUsername(null,username);
        if(sysUser==null){
            SysUser sysUserNew=new SysUser();
            sysUserNew.setId(null);
            sysUserNew.setUsername(username);
            sysUserNew.setName(username);
            sysUserNew.setPassword(new BCryptPasswordEncoder().encode(initPwd));
            Integer[] roleId={};
            Integer[] orgId={};
            userService.addUserExcludeKeycloak(sysUserNew,orgId,roleId);
        }

        Subject currentUser = SecurityUtils.getSubject();
        try {
            currentUser.login(new UsernamePasswordToken(username, ""));
        } catch (UnknownAccountException uae) {
            logHelper.logAuthFail("登录", "用户帐号或密码不正确");
            return ResponseUtil.fail(KEYCLOAK_USER_NO_EXIST, "用户帐号或密码不正确");
        } catch (LockedAccountException lae) {
            logHelper.logAuthFail("登录", "用户帐号已锁定不可用");
            return ResponseUtil.fail(KEYCLOAK_USER_NO_EXIST, "用户帐号已锁定不可用");

        } catch (AuthenticationException ae) {
            logHelper.logAuthFail("登录", "认证失败");
            return ResponseUtil.fail(KEYCLOAK_USER_NO_EXIST, "认证失败");
        }

        currentUser = SecurityUtils.getSubject();


        SysUser user = (SysUser) currentUser.getPrincipal();
        user.setLastLoginIp(IpUtil.getIpAddr(request));
        user.setLastLoginTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userService.updateById(user);

        logHelper.logAuthSucceed("登录");

        // userInfo
        Map<String, Object> adminInfo = new HashMap<String, Object>();
        adminInfo.put("nickName", user.getUsername());
        adminInfo.put("avatar", user.getAvatar());

        Map<Object, Object> result = new HashMap<Object, Object>();
        result.put("token", currentUser.getSession().getId());
        result.put("adminInfo", adminInfo);
        return ResponseUtil.ok(result);
    }

    /*
     *
     */
    @RequiresAuthentication
    @PostMapping("/logout")
    @ApiOperation("退出")
    public Object logout(HttpServletRequest request) {
        try {
            request.logout();
        } catch (ServletException e) {
            return ResponseUtil.fail(e.getMessage());
        }
        Subject currentUser = SecurityUtils.getSubject();

        logHelper.logAuthSucceed("退出");
        currentUser.logout();
        return ResponseUtil.ok();
    }


    @GetMapping("/info")
    @ApiOperation("获取用户信息")
    @RequiresAuthentication
    public Object info(HttpServletRequest request
                       //,@RequestParam String token
    ) {

        Subject currentUser = SecurityUtils.getSubject();
//        currentUser.login(new UsernamePasswordToken(username, ""));
//        currentUser = SecurityUtils.getSubject();

        SysUser user = (SysUser) currentUser.getPrincipal();

        Map<String, Object> data = new HashMap<>();
        data.put("name", user.getUsername());
        data.put("avatar", user.getAvatar());
        data.put("id", user.getId());
        data.put("depts", UserInfo.getDepts());

        List<Integer> roleIds = user.getRoles().stream().map(SysRole::getId).collect(Collectors.toList());
        Set<String> roles = user.getRoles().stream().map(SysRole::getName).collect(Collectors.toSet());
        Set<String> permissions = new HashSet<>();

        permissions = permissionService.list(new QueryWrapper<SysPermission>().lambda().
                in(SysPermission::getRoleId, roleIds).eq(SysPermission::getDeleted, 0)).stream().map(SysPermission::getPermission).collect(Collectors.toSet());

//        Integer[] roleIds=user.getRoleIds();
//        Set<String> roles = new HashSet<>();
//        Set<String> permissions = new HashSet<>();
//        if (roleIds != null && roleIds.length > 0) {
//            roles = roleService.list(new QueryWrapper<SysRole>().lambda().
//                    in(SysRole::getId, roleIds).eq(SysRole::getDeleted, 0)).stream().map(SysRole::getName).collect(Collectors.toSet());
//            permissions = permissionService.list(new QueryWrapper<SysPermission>().lambda().
//                    in(SysPermission::getRoleId, roleIds).eq(SysPermission::getDeleted, 0)).stream().map(SysPermission::getPermission).collect(Collectors.toSet());
//        }
        data.put("roles", roles);
        // NOTE
        // 这里需要转换perms结构，因为对于前端而已API形式的权限更容易理解
        data.put("perms", toApi(permissions));
        data.put("portalUrl", portalUrl);

        return ResponseUtil.ok(data);
    }

    @Autowired
    private ApplicationContext context;
    private HashMap<String, String> systemPermissionsMap = null;

    private Collection<String> toApi(Set<String> permissions) {
        if (systemPermissionsMap == null) {
            systemPermissionsMap = new HashMap<>();
            final String basicPackage = "com.digitalchina.resourcecatalog.admin";
            List<Permission> systemPermissions = PermissionUtil.listPermission(context, basicPackage);
            for (Permission permission : systemPermissions) {
                String perm = permission.getRequiresPermissions().value()[0];
                String api = permission.getApi();
                systemPermissionsMap.put(perm, api);
            }
        }

        Collection<String> apis = new HashSet<>();
        for (String perm : permissions) {
            String api = systemPermissionsMap.get(perm);
            apis.add(api);

            if (perm.equals("*")) {
                apis.clear();
                apis.add("*");
                return apis;
                //                return systemPermissionsMap.values();

            }
        }
        return apis;
    }

    @GetMapping("/401")
    public Object page401() {
        return ResponseUtil.unlogin();
    }

    @GetMapping("/index")
    public Object pageIndex() {
        return ResponseUtil.ok();
    }

    @GetMapping("/403")
    public Object page403() {
        return ResponseUtil.unauthz();
    }
}
