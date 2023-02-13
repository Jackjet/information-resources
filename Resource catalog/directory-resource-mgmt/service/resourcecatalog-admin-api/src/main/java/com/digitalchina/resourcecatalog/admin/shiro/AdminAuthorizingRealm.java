package com.digitalchina.resourcecatalog.admin.shiro;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.digitalchina.resourcecatalog.core.util.JacksonUtil;
import com.digitalchina.resourcecatalog.db.domain.*;
import com.digitalchina.resourcecatalog.db.service.*;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import com.digitalchina.resourcecatalog.core.util.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AdminAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private SysUserService userService;
    //    @Autowired
//    private SysRoleService roleService;
    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private ArchOrgService archOrgService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }
        SysUser user = (SysUser) getAvailablePrincipal(principals);
        List<Integer> roleIds = user.getRoles().stream().map(SysRole::getId).collect(Collectors.toList());
        Set<String> roles = user.getRoles().stream().map(SysRole::getName).collect(Collectors.toSet());
        Set<String> permissions = new HashSet<>();

//        if (roleIds != null && roleIds.length > 0) {
//            roles = roleService.list(new QueryWrapper<SysRole>().lambda().
//                    in(SysRole::getId, roleIds).eq(SysRole::getDeleted, 0)).stream().map(SysRole::getName).collect(Collectors.toSet());
        permissions = permissionService.list(new QueryWrapper<SysPermission>().lambda().
                in(SysPermission::getRoleId, roleIds).eq(SysPermission::getDeleted, 0)).stream().map(SysPermission::getPermission).collect(Collectors.toSet());
//        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(roles);
        info.setStringPermissions(permissions);
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        String username = upToken.getUsername();
//        String password = new String(upToken.getPassword());

        if (StringUtils.isEmpty(username)) {
            throw new AccountException("用户名不能为空");
        }
//        if (StringUtils.isEmpty(password)) {
//            throw new AccountException("密码不能为空");
//        }
//        List<SysUser> userList = userService.list(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername, username).eq(SysUser::getDisabled, 0).eq(SysUser::getDeleted, 0));
//        Assert.state(userList.size() < 2, "同一个用户名存在两个账户");
//        if (userList.size() == 0) {
//            throw new UnknownAccountException("找不到用户（" + username + "）的帐号信息");
//        }
        SysUser admin = userService.findByIdOrUsername(null, username);
        if (admin == null) {
            throw new UnknownAccountException("找不到用户（" + username + "）的帐号信息");
        }
//        List<Integer> collect = userRoleRelService.list(new QueryWrapper<SysUserRoleRel>().lambda()
//                .eq(SysUserRoleRel::getUserId, admin.getId())).stream().map(SysUserRoleRel::getRoleId).collect(Collectors.toList());
//        Integer[] roleIds = new Integer[collect.size()];
//        for (int i = 0; i < collect.size(); i++) {
//            roleIds[i] = collect.get(i);
//        }
//        admin.setRoleIds(roleIds);
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        if (!encoder.matches(password, admin.getPassword())) {
//            throw new UnknownAccountException("找不到用户（" + username + "）的帐号信息");
//        }else{
        if(admin.getOrgs()!=null && admin.getOrgs().size()>0)
            //获取部门的id
            admin.setDepts(archOrgService.getDeptsByOrgIds(admin.getOrgs()));
//        }
        return new SimpleAuthenticationInfo(admin, admin.getPassword(), getName());
    }
    @Override
    protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info)
            throws AuthenticationException {
    }
}
