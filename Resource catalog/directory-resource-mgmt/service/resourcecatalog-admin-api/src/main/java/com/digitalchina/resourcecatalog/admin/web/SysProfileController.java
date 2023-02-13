package com.digitalchina.resourcecatalog.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.digitalchina.resourcecatalog.admin.util.AdminResponseCode;
import com.digitalchina.resourcecatalog.core.util.JacksonUtil;
import com.digitalchina.resourcecatalog.core.util.ResponseUtil;
import com.digitalchina.resourcecatalog.core.util.bcrypt.BCryptPasswordEncoder;
import com.digitalchina.resourcecatalog.db.domain.SysUser;
import com.digitalchina.resourcecatalog.db.service.SysUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/admin/profile")
@Validated
@Api(value = "登录用户接口", tags = "登录用户接口")
public class SysProfileController {
    private final Log logger = LogFactory.getLog(SysProfileController.class);

    @Autowired
    private SysUserService userService;

    @RequiresAuthentication
    @PostMapping("/password")
    @ApiOperation("修改密码")
    public Object create(@RequestBody String body) {
        String oldPassword = JacksonUtil.parseString(body, "oldPassword");
        String newPassword = JacksonUtil.parseString(body, "newPassword");
        if (StringUtils.isEmpty(oldPassword)) {
            return ResponseUtil.badArgument();
        }
        if (StringUtils.isEmpty(newPassword)) {
            return ResponseUtil.badArgument();
        }

        Subject currentUser = SecurityUtils.getSubject();
        SysUser admin = (SysUser) currentUser.getPrincipal();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(oldPassword, admin.getPassword())) {
            return ResponseUtil.fail(AdminResponseCode.ADMIN_INVALID_ACCOUNT, "账号密码不对");
        }

        String encodedNewPassword = encoder.encode(newPassword);
        admin.setPassword(encodedNewPassword);
        userService.updateById(admin);
        return ResponseUtil.ok();
    }
}
