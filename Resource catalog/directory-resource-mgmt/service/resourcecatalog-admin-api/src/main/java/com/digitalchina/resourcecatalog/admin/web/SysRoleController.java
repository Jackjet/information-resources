package com.digitalchina.resourcecatalog.admin.web;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.admin.annotation.RequiresPermissionsDesc;
import com.digitalchina.resourcecatalog.admin.util.AdminResponseCode;
import com.digitalchina.resourcecatalog.admin.util.Permission;
import com.digitalchina.resourcecatalog.admin.util.PermissionUtil;
import com.digitalchina.resourcecatalog.admin.vo.PermVo;
import com.digitalchina.resourcecatalog.core.util.JacksonUtil;
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.db.domain.*;
import com.digitalchina.resourcecatalog.db.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.digitalchina.resourcecatalog.admin.util.AdminResponseCode.ROLE_NAME_EXIST;
import static com.digitalchina.resourcecatalog.admin.util.AdminResponseCode.ROLE_USER_EXIST;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author wangmh
 * @since 2020-04-30
 */
@RestController
@RequestMapping("/admin/role")
@Validated
@Api(value = "角色管理", tags = "角色管理")
public class SysRoleController {

    private final Log logger = LogFactory.getLog(SysRoleController.class);

    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysPermissionService permissionService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysUserRoleRelService userRoleService;
    @RequiresPermissions("admin:role:list")
    @RequiresPermissionsDesc(menu = {"用户管理", "角色管理"}, order = 20,  button = "角色查询")
    @GetMapping("/list")
    @ApiOperation("分页")
    public Object list(String name,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit) {
        Page page1 = new Page<>(page, limit);
        return ResponseUtil.ok(roleService.page(page1, new QueryWrapper<SysRole>().lambda().like(SysRole::getName, name)
                .eq(SysRole::getDeleted, 0).orderByDesc(SysRole::getAddTime)));
    }

    @GetMapping("/options")
    @ApiOperation("选项")
    public Object options() {
        List<SysRole> roleList = roleService.list(new QueryWrapper<SysRole>().lambda().eq(SysRole::getDeleted, 0));

        List<Map<String, Object>> options = new ArrayList<>(roleList.size());
        for (SysRole role : roleList) {
            Map<String, Object> option = new HashMap<>(2);
            option.put("value", role.getId());
            option.put("label", role.getName());
            options.add(option);
        }

        return ResponseUtil.okList(options);
    }

    @RequiresPermissions("admin:role:read")
    @RequiresPermissionsDesc(menu = {"用户管理", "角色管理"}, order = 20,  button = "角色详情")
    @GetMapping("/read")
    @ApiOperation("详情")
    public Object read(@RequestParam(required=true) Integer id) {
        SysRole role = roleService.getById(id);
        return ResponseUtil.ok(role);
    }


    private Object validate(SysRole role) {
        String name = role.getName();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }

        return null;
    }

    @RequiresPermissions("admin:role:create")
    @RequiresPermissionsDesc(menu = {"用户管理", "角色管理"}, order = 20,  button = "角色添加")
    @PostMapping("/create")
    @ApiOperation("保存")
    public Object create(@RequestBody SysRole role) {
        Object error = validate(role);
        if (error != null) {
            return error;
        }

        if (roleService.count(new QueryWrapper<SysRole>().lambda().eq(SysRole::getName, role.getName()).eq(SysRole::getDeleted, 0)) > 0) {
            return ResponseUtil.fail(ROLE_NAME_EXIST, "角色已经存在");
        }

        role.setAddTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        roleService.save(role);

        return ResponseUtil.ok(role);
    }

    @RequiresPermissions("admin:role:update")
    @RequiresPermissionsDesc(menu = {"用户管理", "角色管理"}, order = 20,  button = "角色编辑")
    @PostMapping("/update")
    @ApiOperation("更新")
    public Object update(@RequestBody SysRole role) {
        Object error = validate(role);
        if (error != null) {
            return error;
        }
        if (roleService.count(new QueryWrapper<SysRole>().lambda().eq(SysRole::getName, role.getName()).eq(SysRole::getDeleted, 0).ne(SysRole::getId, role.getId())) > 0) {
            return ResponseUtil.fail(ROLE_NAME_EXIST, "角色已经存在");
        }
        role.setUpdateTime(LocalDateTime.now());
        roleService.updateById(role);
        return ResponseUtil.ok();
    }

    @RequiresPermissions("admin:role:delete")
    @RequiresPermissionsDesc(menu = {"用户管理", "角色管理"}, order = 20,  button = "角色删除")
    @PostMapping("/delete")
    @ApiOperation("删除")
    public Object delete(@RequestBody SysRole role) {
        Integer id = role.getId();
        if (id == null) {
            return ResponseUtil.badArgument();
        }

        // 如果当前角色所对应管理员仍存在，则拒绝删除角色。
        List<SysUserRoleRel> srList = userRoleService.list(new QueryWrapper<SysUserRoleRel>().lambda().eq(SysUserRoleRel::getRoleId, id));
        if(srList!=null && srList.size()>0){
        	List<Integer> uIdList = srList.stream().map( sr -> sr.getUserId()).collect(Collectors.toList());
        	List<SysUser> sysUserList = userService.list(new QueryWrapper<SysUser>().lambda().eq(SysUser::getDeleted, 0).in(SysUser::getId, uIdList));
        	if (sysUserList!=null && sysUserList.size()>0) {
        		return ResponseUtil.fail(ROLE_USER_EXIST, "当前角色存在管理员，不能删除");
        	}
        }
        role.setDeleted(1);
        roleService.updateById(role);
        return ResponseUtil.ok();
    }


    @Autowired
    private ApplicationContext context;
    private List<PermVo> systemPermissions = null;
    private Set<String> systemPermissionsString = null;

    private List<PermVo> getSystemPermissions() {
        final String basicPackage = "com.digitalchina.resourcecatalog.admin";
        if (systemPermissions == null) {
            List<Permission> permissions = PermissionUtil.listPermission(context, basicPackage);
            systemPermissions = PermissionUtil.listPermVo(permissions);
            systemPermissionsString = PermissionUtil.listPermissionString(permissions);
        }
        return systemPermissions;
    }

    private Set<String> getAssignedPermissions(Integer roleId) {
        // 这里需要注意的是，如果存在超级权限*，那么这里需要转化成当前所有系统权限。
        // 之所以这么做，是因为前端不能识别超级权限，所以这里需要转换一下。
        Set<String> assignedPermissions = null;
        if (permissionService.count(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getRoleId, roleId)
                .eq(SysPermission::getPermission, "*").eq(SysPermission::getDeleted, 0)) > 0) {
            getSystemPermissions();
            assignedPermissions = systemPermissionsString;
        } else {
            assignedPermissions = permissionService.list(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getRoleId, roleId)
                    .eq(SysPermission::getDeleted, 0)).stream().map(SysPermission::getPermission).collect(Collectors.toSet());
        }
        return assignedPermissions;
    }

    /**
     * 管理员的权限情况
     *
     * @return 系统所有权限列表和管理员已分配权限
     */
    @RequiresPermissions("admin:role:permission:get")
    @RequiresPermissionsDesc(menu = {"用户管理", "角色管理"}, order = 20,  button = "权限详情")
    @GetMapping("/permissions")
    @ApiOperation("管理员的权限情况")
    public Object getPermissions(Integer roleId) {
        List<PermVo> systemPermissions = getSystemPermissions();
        Set<String> assignedPermissions = getAssignedPermissions(roleId);

        Map<String, Object> data = new HashMap<>();
        data.put("systemPermissions", systemPermissions);
        data.put("assignedPermissions", assignedPermissions);
        return ResponseUtil.ok(data);
    }


    /**
     * 更新管理员的权限
     *
     * @param body
     * @return
     */
    @RequiresPermissions("admin:role:permission:update")
    @RequiresPermissionsDesc(menu = {"用户管理", "角色管理"}, order = 20,  button = "权限变更")
    @PostMapping("/permissions")
    @ApiOperation("更新管理员的权限")
    public Object updatePermissions(@RequestBody String body) {
        Integer roleId = JacksonUtil.parseInteger(body, "roleId");
        List<String> permissions = JacksonUtil.parseStringList(body, "permissions");
        if (roleId == null || permissions == null) {
            return ResponseUtil.badArgument();
        }

        // 如果修改的角色是超级权限，则拒绝修改。
        if (permissionService.count(new QueryWrapper<SysPermission>().lambda().eq(SysPermission::getRoleId, roleId)
                .eq(SysPermission::getPermission, "*").eq(SysPermission::getDeleted, 0)) > 0) {
            return ResponseUtil.fail(AdminResponseCode.ROLE_SUPER_SUPERMISSION, "当前角色的超级权限不能变更");
        }

        // 先删除旧的权限，再更新新的权限
        SysPermission sysPermission = new SysPermission();
        sysPermission.setDeleted(1);
        permissionService.update(sysPermission, new UpdateWrapper<SysPermission>().lambda().eq(SysPermission::getRoleId, roleId).eq(SysPermission::getDeleted, 0));
        List<SysPermission> psList = new ArrayList<SysPermission>(permissions.size());
        for (String permission : permissions) {
            SysPermission sysPermission1 = new SysPermission();
            sysPermission1.setRoleId(roleId);
            sysPermission1.setPermission(permission);
            sysPermission1.setAddTime(LocalDateTime.now());
            sysPermission1.setUpdateTime(LocalDateTime.now());
            sysPermission1.setDeleted(0);
            psList.add(sysPermission1);
        }
        permissionService.saveBatch(psList);
        return ResponseUtil.ok();
    }

}
