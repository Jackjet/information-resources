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
 * ???????????? ???????????????
 * </p>
 *
 * @author wangmh
 * @since 2020-04-30
 */
@RestController
@RequestMapping("/admin/user")
@Validated
@Api(value = "????????????", tags = "????????????")
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
    @RequiresPermissionsDesc(menu = {"????????????", "????????????"}, order = 20, order2 = 9, button = "??????")
    @GetMapping("/list")
    public Object list(String name, Integer orgId, Integer roleId,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit) {
        Page page1 = new Page<>(page, limit);

        IPage<List<Map>> users=userService.myPage(page1, name, orgId, roleId);

        //??????????????? orgId=0???????????????????????????
        for (Object userMap: users.getRecords()) {
            HashMap<String,Object> user=(HashMap<String,Object>)userMap;

            if(user.get("orgId")!=null&&(Integer) user.get("orgId")==0){
                ((HashMap<String, Object>) userMap).put("orgnm",initDept);
            }
        }
        return ResponseUtil.ok(users);
    }

    @RequiresPermissions("admin:user:resetpwd")
    @RequiresPermissionsDesc(menu = {"????????????", "????????????"}, order = 20, order2 = 9, button = "????????????")
    @PostMapping("/resetpwd")
    @ApiOperation("????????????")
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
        logHelper.logAuthSucceed("????????????", sysUser.getUsername());
        return ResponseUtil.ok(initPwd);
    }

    @RequiresPermissions("admin:user:create")
    @RequiresPermissionsDesc(menu = {"????????????", "????????????"}, order = 20, order2 = 9, button = "??????")
    @PostMapping("/create")
    @ApiOperation("??????")
    public Object create(@Valid @RequestBody SysUserDto sysUserDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseUtil.fail(400, result.getFieldError().getDefaultMessage());
        }
        if (!RegexUtil.isUsername(sysUserDto.getUsername())) {
            return ResponseUtil.fail(ADMIN_INVALID_NAME, "??????????????????????????????");
        }
        sysUserDto.setId(null);
        SysUser user = BeanUtils.dtoToDo(sysUserDto, SysUser.class);
        String username = user.getUsername();
        List<SysUser> list = userService.list(new QueryWrapper<SysUser>().lambda()
                .eq(SysUser::getUsername, username).eq(SysUser::getDeleted, 0));
        if (list.size() > 0) {
            return ResponseUtil.fail(ADMIN_NAME_EXIST, "?????????????????????");
        }
        user.setPassword(new BCryptPasswordEncoder().encode(initPwd));
        userService.addUser(user, sysUserDto.getOrgId(), sysUserDto.getRoleId());
        logHelper.logAuthSucceed("???????????????", username);
        return ResponseUtil.ok(user);
    }

    @RequiresPermissions("admin:user:read")
    @RequiresPermissionsDesc(menu = {"????????????", "????????????"}, order = 20, order2 = 9, button = "??????")
    @GetMapping("/read")
    @ApiOperation("??????")
    public Object read(@RequestParam Integer id) {
        return ResponseUtil.ok(userService.findByIdOrUsername(id, null));
    }

    @RequiresPermissions("admin:user:update")
    @RequiresPermissionsDesc(menu = {"????????????", "????????????"}, order = 20, order2 = 9, button = "??????")
    @PostMapping("/update")
    @ApiOperation("??????")
    public Object update(@Valid @RequestBody SysUserDto sysUserDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseUtil.fail(400, result.getFieldError().getDefaultMessage());
        }
        if (StringUtils.isEmpty(sysUserDto.getId())) {
            return ResponseUtil.fail(400, "id????????????");
        }
        if (!RegexUtil.isUsername(sysUserDto.getUsername())) {
            return ResponseUtil.fail(ADMIN_INVALID_NAME, "??????????????????????????????");
        }
        SysUser user = BeanUtils.dtoToDo(sysUserDto, SysUser.class);
        if (!userService.updateInfo(user, sysUserDto.getOrgId(), sysUserDto.getRoleId())) {
            return ResponseUtil.updatedDataFailed();
        }
        logHelper.logAuthSucceed("???????????????", user.getUsername());
        return ResponseUtil.ok();
    }

    @RequiresPermissions("admin:user:delete")
    @RequiresPermissionsDesc(menu = {"????????????", "????????????"}, order = 20, order2 = 9, button = "??????")
    @PostMapping("/delete")
    @ApiOperation("??????")
    public Object delete(@RequestBody Map<String, Integer[]> map) {
        Integer[] ids = map.get(SysUser.ID);
        if (ids == null || ids.length < 1) {
            return ResponseUtil.badArgument();
        }
        SysUser user = new SysUser();
        for (Integer id : ids) {
            user.setId(id);
            // ?????????????????????????????????
            Subject currentUser = SecurityUtils.getSubject();
            SysUser currentAdmin = (SysUser) currentUser.getPrincipal();
            if (currentAdmin.getId().equals(id)) {
                return ResponseUtil.fail(ADMIN_DELETE_NOT_ALLOWED, "?????????????????????????????????");
            }
            user.setDeleted(1);
            userService.delete(user);
            logHelper.logAuthSucceed("???????????????", user.getUsername());
        }
        return ResponseUtil.ok();
    }

    @RequiresPermissions("admin:user:isDisabled")
    @RequiresPermissionsDesc(menu = {"????????????", "????????????"}, order = 20, order2 = 9, button = "??????/??????")
    @PostMapping("/isDisabled")
    @ApiOperation("??????/????????????")
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
        logHelper.logAuthSucceed(disable == 0 ? "????????????" : "????????????");
        return ResponseUtil.ok();
    }

    @RequiresPermissions("admin:user:setCataRole")
    @RequiresPermissionsDesc(menu = {"????????????", "????????????"}, order = 20, order2 = 9, button = "??????")
    @PostMapping("/setCataRole")
    @ApiOperation("??????????????????????????????")
    public Object setCataRole(@RequestBody Map<String, Object> map) {
        Integer id = Integer.parseInt(map.get(SysUser.ID).toString());
        if (id == null) {
            return ResponseUtil.fail(400, "??????id????????????");
        }
        List<HashMap> typeIds = CollectUtils.castList(map.get("typeIds"), HashMap.class);

        if (typeIds == null || typeIds.size() < 1) {
            return ResponseUtil.fail(400, "????????????id????????????");
        }
        userService.setCataRole(id, typeIds);
        logHelper.logAuthSucceed("??????????????????????????????", UserInfo.getUsername());
        return ResponseUtil.ok();
    }

}
