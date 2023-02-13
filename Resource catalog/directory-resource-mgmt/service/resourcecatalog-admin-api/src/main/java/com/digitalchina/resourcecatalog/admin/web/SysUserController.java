package com.digitalchina.resourcecatalog.admin.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.digitalchina.resourcecatalog.admin.annotation.RequiresPermissionsDesc;
import com.digitalchina.resourcecatalog.admin.dto.SysUserDto;
import com.digitalchina.resourcecatalog.admin.service.LogHelper;
import com.digitalchina.resourcecatalog.admin.util.UserInfo;
import com.digitalchina.resourcecatalog.core.util.BeanUtils;
import com.digitalchina.resourcecatalog.core.util.CollectUtils;
import com.digitalchina.resourcecatalog.core.util.RegexUtil;
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.core.util.bcrypt.BCryptPasswordEncoder;
import com.digitalchina.resourcecatalog.db.domain.SysUser;
import com.digitalchina.resourcecatalog.db.service.SysUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.digitalchina.resourcecatalog.admin.util.AdminResponseCode.*;
import static com.digitalchina.resourcecatalog.admin.util.AdminResponseCode.ADMIN_DELETE_NOT_ALLOWED;

/**
 * <p>
 * 管理员表 前端控制器
 * </p>
 *
 * @author wangmh
 * @since 2020-04-30
 */
@RestController
@RequestMapping("/admin/user")
@Validated
@Api(value = "用户管理", tags = "用户管理")
public class SysUserController {
    private final Log logger = LogFactory.getLog(SysUserController.class);

    @Value("${resourcecatalog.user.init.password}")
    private String initPwd;

    @Autowired
    private SysUserService userService;

    @Autowired
    private LogHelper logHelper;

    @Value("${resourcecatalog.tree.init.dept}")
    private String initDept;

    @RequiresPermissions("admin:user:list")
    @RequiresPermissionsDesc(menu = {"用户管理", "用户管理"}, order = 20, order2 = 9, button = "查询")
    @GetMapping("/list")
    public Object list(String name, Integer orgId, Integer roleId,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit) {
        Page page1 = new Page<>(page, limit);

        IPage<List<Map>> users=userService.myPage(page1, name, orgId, roleId);

        //格式化数据 orgId=0时转换为配置的名称
        for (Object userMap: users.getRecords()) {
            HashMap<String,Object> user=(HashMap<String,Object>)userMap;

            if(user.get("orgId")!=null&&(Integer) user.get("orgId")==0){
                ((HashMap<String, Object>) userMap).put("orgnm",initDept);
            }
        }
        return ResponseUtil.ok(users);
    }

    @RequiresPermissions("admin:user:resetpwd")
    @RequiresPermissionsDesc(menu = {"用户管理", "用户管理"}, order = 20, order2 = 9, button = "重置密码")
    @PostMapping("/resetpwd")
    @ApiOperation("重置密码")
    public Object resetpwd(@RequestBody Map<String, Integer[]> map) {
        Integer[] ids = map.get(SysUser.ID);
        if (ids == null || ids.length < 1) {
            return ResponseUtil.badArgument();
        }
        SysUser sysUser = new SysUser();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(initPwd);
        sysUser.setPassword(encodedPassword);
        sysUser.setUpdateTime(LocalDateTime.now());
        UpdateWrapper<SysUser> wrapper = new UpdateWrapper<>();
        wrapper.lambda().in(SysUser::getId, ids);
        userService.update(sysUser, wrapper);
        logHelper.logAuthSucceed("重置密码", sysUser.getUsername());
        return ResponseUtil.ok(initPwd);
    }

    @RequiresPermissions("admin:user:create")
    @RequiresPermissionsDesc(menu = {"用户管理", "用户管理"}, order = 20, order2 = 9, button = "添加")
    @PostMapping("/create")
    @ApiOperation("保存")
    public Object create(@Valid @RequestBody SysUserDto sysUserDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseUtil.fail(400, result.getFieldError().getDefaultMessage());
        }
        if (!RegexUtil.isUsername(sysUserDto.getUsername())) {
            return ResponseUtil.fail(ADMIN_INVALID_NAME, "管理员名称不符合规定");
        }
        sysUserDto.setId(null);
        SysUser user = BeanUtils.dtoToDo(sysUserDto, SysUser.class);
        String username = user.getUsername();
        List<SysUser> list = userService.list(new QueryWrapper<SysUser>().lambda()
                .eq(SysUser::getUsername, username).eq(SysUser::getDeleted, 0));
        if (list.size() > 0) {
            return ResponseUtil.fail(ADMIN_NAME_EXIST, "管理员已经存在");
        }
        user.setPassword(new BCryptPasswordEncoder().encode(initPwd));
        userService.addUser(user, sysUserDto.getOrgId(), sysUserDto.getRoleId());
        logHelper.logAuthSucceed("添加管理员", username);
        return ResponseUtil.ok(user);
    }

    @RequiresPermissions("admin:user:read")
    @RequiresPermissionsDesc(menu = {"用户管理", "用户管理"}, order = 20, order2 = 9, button = "详情")
    @GetMapping("/read")
    @ApiOperation("详情")
    public Object read(@RequestParam Integer id) {
        return ResponseUtil.ok(userService.findByIdOrUsername(id, null));
    }

    @RequiresPermissions("admin:user:update")
    @RequiresPermissionsDesc(menu = {"用户管理", "用户管理"}, order = 20, order2 = 9, button = "编辑")
    @PostMapping("/update")
    @ApiOperation("更新")
    public Object update(@Valid @RequestBody SysUserDto sysUserDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseUtil.fail(400, result.getFieldError().getDefaultMessage());
        }
        if (StringUtils.isEmpty(sysUserDto.getId())) {
            return ResponseUtil.fail(400, "id不能为空");
        }
        if (!RegexUtil.isUsername(sysUserDto.getUsername())) {
            return ResponseUtil.fail(ADMIN_INVALID_NAME, "管理员名称不符合规定");
        }
        SysUser user = BeanUtils.dtoToDo(sysUserDto, SysUser.class);
        if (!userService.updateInfo(user, sysUserDto.getOrgId(), sysUserDto.getRoleId())) {
            return ResponseUtil.updatedDataFailed();
        }
        logHelper.logAuthSucceed("编辑管理员", user.getUsername());
        return ResponseUtil.ok();
    }

    @RequiresPermissions("admin:user:delete")
    @RequiresPermissionsDesc(menu = {"用户管理", "用户管理"}, order = 20, order2 = 9, button = "删除")
    @PostMapping("/delete")
    @ApiOperation("删除")
    public Object delete(@RequestBody Map<String, Integer[]> map) {
        Integer[] ids = map.get(SysUser.ID);
        if (ids == null || ids.length < 1) {
            return ResponseUtil.badArgument();
        }
        SysUser user = new SysUser();
        for (Integer id : ids) {
            user.setId(id);
            // 管理员不能删除自身账号
            Subject currentUser = SecurityUtils.getSubject();
            SysUser currentAdmin = (SysUser) currentUser.getPrincipal();
            if (currentAdmin.getId().equals(id)) {
                return ResponseUtil.fail(ADMIN_DELETE_NOT_ALLOWED, "管理员不能删除自己账号");
            }
            user.setDeleted(1);
            userService.delete(user);
            logHelper.logAuthSucceed("删除管理员", user.getUsername());
        }
        return ResponseUtil.ok();
    }

    @RequiresPermissions("admin:user:isDisabled")
    @RequiresPermissionsDesc(menu = {"用户管理", "用户管理"}, order = 20, order2 = 9, button = "禁用/启用")
    @PostMapping("/isDisabled")
    @ApiOperation("禁用/启用用户")
    public Object isDisabled(@RequestBody Map<String, Integer> map) {
        Integer anotherAdminId = map.get(SysUser.ID);
        if (anotherAdminId == null) {
            return ResponseUtil.badArgument();
        }
        SysUser sysUser = new SysUser();
        sysUser.setId(anotherAdminId);
        Integer disable = map.get(SysUser.DISABLED);
        sysUser.setDisabled(disable);
        sysUser.setUpdateTime(LocalDateTime.now());
        userService.updateById(sysUser);
        logHelper.logAuthSucceed(disable == 0 ? "启用用户" : "禁用用户");
        return ResponseUtil.ok();
    }

    @RequiresPermissions("admin:user:setCataRole")
    @RequiresPermissionsDesc(menu = {"用户管理", "用户管理"}, order = 20, order2 = 9, button = "权限")
    @PostMapping("/setCataRole")
    @ApiOperation("设置目录分类管理权限")
    public Object setCataRole(@RequestBody Map<String, Object> map) {
        Integer id = Integer.parseInt(map.get(SysUser.ID).toString());
        if (id == null) {
            return ResponseUtil.fail(400, "用户id不能为空");
        }
        List<HashMap> typeIds = CollectUtils.castList(map.get("typeIds"), HashMap.class);

        if (typeIds == null || typeIds.size() < 1) {
            return ResponseUtil.fail(400, "目录分类id不能为空");
        }
        userService.setCataRole(id, typeIds);
        logHelper.logAuthSucceed("设置目录分类管理权限", UserInfo.getUsername());
        return ResponseUtil.ok();
    }

}
