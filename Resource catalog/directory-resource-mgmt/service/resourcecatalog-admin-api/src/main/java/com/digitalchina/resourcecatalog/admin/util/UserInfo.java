package com.digitalchina.resourcecatalog.admin.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.digitalchina.resourcecatalog.db.domain.SysRole;
import com.digitalchina.resourcecatalog.db.domain.SysUser;

/**
 * @author: create by wangmh
 * @name: UserInfo.java
 * @description:
 * @date:2020/4/1
 **/
public class UserInfo {
	
	public static final Integer SUPER_ADMIN_ROLE = 1;
	public static final Integer RES_ADMIN_ROLE = 2;
	public static final Integer DEPT_ADMIN_ROLE = 3;
	
    public static String getUsername() {
        Subject currentUser = SecurityUtils.getSubject();
        SysUser admin = (SysUser) currentUser.getPrincipal();
        return admin.getUsername();
    }
    
    public static String getName() {
        Subject currentUser = SecurityUtils.getSubject();
        SysUser admin = (SysUser) currentUser.getPrincipal();
        return admin.getName();
    }

    public static Integer getId() {
        Subject currentUser = SecurityUtils.getSubject();
        SysUser admin = (SysUser) currentUser.getPrincipal();
        return admin.getId();
    }
    public static boolean isSuperAdmin(){
    	Subject currentUser = SecurityUtils.getSubject();
        SysUser admin = (SysUser) currentUser.getPrincipal();
        return hasTargetRole(admin.getRoles(), SUPER_ADMIN_ROLE);
    }
    public static boolean isResAdmin(){
    	Subject currentUser = SecurityUtils.getSubject();
        SysUser admin = (SysUser) currentUser.getPrincipal();
        return hasTargetRole(admin.getRoles(), RES_ADMIN_ROLE);
    }
    public static boolean isDeptAdmin(){
    	Subject currentUser = SecurityUtils.getSubject();
        SysUser admin = (SysUser) currentUser.getPrincipal();
        return hasTargetRole(admin.getRoles(), DEPT_ADMIN_ROLE);
    }
    public static List<Integer> getDeptIds(){
    	Subject currentUser = SecurityUtils.getSubject();
        SysUser admin = (SysUser) currentUser.getPrincipal();
        if(admin.getDepts() != null){
        	return admin.getDepts().stream().map(dept -> dept.getKey()).collect(Collectors.toList());
        }
        return new ArrayList<Integer>(0);
    }
    public static List<DefaultKeyValue<Integer, String>> getDepts(){
    	Subject currentUser = SecurityUtils.getSubject();
        SysUser admin = (SysUser) currentUser.getPrincipal();
        return	admin.getDepts();
    }
    public static boolean hasTargetRole(List<SysRole> roleList, Integer targetRoleId){
    	if(roleList==null) return false;
    	return roleList.stream().map(SysRole :: getId).collect(Collectors.toList()).contains(targetRoleId);
    }
}
