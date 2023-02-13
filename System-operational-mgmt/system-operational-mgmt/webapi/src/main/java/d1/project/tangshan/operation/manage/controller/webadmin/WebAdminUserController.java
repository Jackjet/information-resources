package d1.project.tangshan.operation.manage.controller.webadmin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.framework.webapi.model.SignInResult;
import d1.project.tangshan.operation.manage.entity.WebAdminUser;
import d1.project.tangshan.operation.manage.entity.operations.module.Services;
import d1.project.tangshan.operation.manage.service.WebAdminService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotBlank;

/**
 * @author Buter
 * @date 2020/3/18 17:12
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/user")
@Api(value = "/webadmin/user", tags = "管理账号管理")
public class WebAdminUserController {
    private final WebAdminService userService;

    @Autowired
    public WebAdminUserController(WebAdminService userService) {
        this.userService = userService;
    }

    @Auth("noauth")
    @ApiOperation(value = "用户登录", notes = "根据用户名密码登录")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", defaultValue = "e10adc3949ba59abbe56e057f20f883e", required = true, dataType = "String")})
    @RequestMapping(value = "/signIn", method = RequestMethod.GET)
    public Result<SignInResult> signIn(String username, String password, HttpServletRequest request) {
        try {
            SignInResult result = userService.signIn(username, password, request);
            return ResultUtil.success("登录成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @Auth("noauth")
    @ApiOperation(value = "用户登录sso")
    @PostMapping(value = "/ssoSignIn")
    public Result<SignInResult> ssoSignIn(@Valid @RequestBody TokenVm tokenVm, HttpServletRequest request) {
        try {
            SignInResult result = userService.ssoSignIn(tokenVm.getSsoToken(), request);
            return ResultUtil.success("登录成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "修改密码", notes = "修改密码")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.PUT)
    public Result<String> updatePassword(HttpServletRequest request, @RequestBody UpdatePwdVm model) {
        try {
            this.userService.updatePassword(request, model.getId(), model.getOldPassword(), model.getNewPassword(), model.getRealPassword(), model.getRealOldPassword());
            return ResultUtil.success("修改成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "注销登录", notes = "注销登录")
    @RequestMapping(value = "/signOut", method = RequestMethod.GET)
    public Result<String> signOut(HttpServletRequest request) {
        try {

            userService.singOut(request);
            return ResultUtil.success("注销成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "新增")
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody WebAdminInsertVm insert, HttpServletRequest request) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(insert);
            userService.insert(jsonObject, request);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) {
        try {
            userService.delete(id, request);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "用户编辑")
    @PutMapping(value = "/update")
    public Result<String> update(HttpServletRequest request, @Valid @RequestBody WebAdminUpdateVm model) {
        try {
            userService.update(request, (JSONObject) JSON.toJSON(model));
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "用户列表（分页）")
    @GetMapping(value = "/findAll")
    public Result<Page<WebAdminUser>> findAll(WebAdminFindAllVm model) {
        try {
            Page<WebAdminUser> result = userService.findAll((JSONObject) JSON.toJSON(model));
            for (WebAdminUser webAdminUser : result) {
                webAdminUser.setPassword("");
            }
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<WebAdminUser> findById(String id) {
        try {
            WebAdminUser webAdminUser = userService.findById(id).orElseThrow(() -> new ValidException("该用户不存在"));
            webAdminUser.setPassword("");
            return ResultUtil.success("成功", userService.findById(id));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "启用/禁用")
    @PutMapping(value = "/updateStatus")
    public Result<String> updateStatus(@Valid @RequestBody WebAdminUpdateStatusVm model, HttpServletRequest request) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            userService.updateStatus(jsonObject, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "重置密码")
    @RequestMapping(value = "/resetPassword", method = RequestMethod.PUT)
    public Result<String> resetPassword(HttpServletRequest request, @RequestBody ResetPwdVm model) {
        try {
            this.userService.resetPassword(request, model.getId());
            return ResultUtil.success("重置密码成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    /**
     * 获取当前登录用户
     */
    @Auth("noauth")
    @GetMapping(value = {"/getLoginUser"})
    public Result<String> getLoginUser(HttpServletRequest request) throws Exception {
        try {
            return ResultUtil.success("", userService.getLoginUser(request));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    /**
     * 清空SsoCookie
     */
    @Auth("noauth")
    @GetMapping(value = {"/clearSsoCookie"})
    public Result<String> clearSsoCookie(HttpServletRequest request, HttpServletResponse response) throws Exception {
        userService.clearSsoCookie(request, response);
        return ResultUtil.success("");
    }
}

class UpdatePwdVm {
    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty(value = "id")
    private String id;
    @NotBlank(message = "旧的密码不能为空")
    @ApiModelProperty(value = "旧密码")
    private String oldPassword;
    @NotBlank(message = "旧的密码不能为空")
    @ApiModelProperty(value = "加密前旧密码")
    private String realOldPassword;
    @NotBlank(message = "新的密码不能为空")
    @ApiModelProperty(value = "新密码")
    private String newPassword;
    @NotBlank(message = "新的密码不能为空")
    @ApiModelProperty(value = "加密前密码")
    private String realPassword;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getRealOldPassword() {
        return realOldPassword;
    }

    public void setRealOldPassword(String realOldPassword) {
        this.realOldPassword = realOldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRealPassword() {
        return realPassword;
    }

    public void setRealPassword(String realPassword) {
        this.realPassword = realPassword;
    }
}

@ApiModel(value = "WebAdminInsertVm", description = "新增")
class WebAdminInsertVm {
    @ApiModelProperty("名称")
    @NotBlank(message = "用户名不可为空")
    private String name;

    @ApiModelProperty("手机号码")
    private String tel;

    @ApiModelProperty("单位")
    private String organization;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("角色Id")
    @NotBlank(message = "角色Id不可为空")
    private String roleId;

    @ApiModelProperty("角色名称")
    @NotBlank(message = "角色名称不可为空")
    private String roleName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}

@ApiModel(value = "WebAdminUpdateVm", description = "编辑")
class WebAdminUpdateVm {
    @ApiModelProperty("用户id")
    @NotBlank(message = "用户id不可为空")
    private String id;

    @ApiModelProperty("手机号码")
    private String tel;

    @ApiModelProperty("单位")
    private String organization;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("角色Id")
    @NotBlank(message = "角色Id不可为空")
    private String roleId;

    @ApiModelProperty("角色名称")
    @NotBlank(message = "角色名称不可为空")
    private String roleName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}

@ApiModel(value = "WebAdminFindAllVm", description = "查询")
class WebAdminFindAllVm extends PageableVm {
    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("手机号码")
    private String tel;

    @ApiModelProperty("单位")
    private String organization;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("角色Id")
    private String roleId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}

@ApiModel(value = "WebAdminUpdateStatusVm", description = "启禁用")
class WebAdminUpdateStatusVm {

    @ApiModelProperty("id")
    @NotBlank(message = "用户id不可为空")
    private String id;

    @ApiModelProperty("状态(0禁用1启用)")
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

class ResetPwdVm {
    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty(value = "id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

class TokenVm {
    @NotBlank(message = "ssoToken不能为空")
    @ApiModelProperty(value = "ssoToken")
    private String ssoToken;

    public String getSsoToken() {
        return ssoToken;
    }

    public void setSsoToken(String ssoToken) {
        this.ssoToken = ssoToken;
    }
}