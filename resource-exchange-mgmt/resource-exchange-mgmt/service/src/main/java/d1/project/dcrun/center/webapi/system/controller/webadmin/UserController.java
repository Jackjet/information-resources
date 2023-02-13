package d1.project.dcrun.center.webapi.system.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.system.model.user.SignInResult;
import d1.project.dcrun.center.webapi.system.service.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author libin
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/webAdminUser")
@Api(value = "/webadmin/webAdminUser", tags = "账户管理")
public class UserController {
    @Autowired
    private UserService userService;

    @Auth("noauth")
    @ApiOperation(value = "用户登录", notes = "根据用户名密码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String"),
            @ApiImplicitParam(name = "type", value = "身份，管理员：webadmin，租户：tenants，项目管理员：integration", required = true, dataType = "String")
    })
    @RequestMapping(value = "/signIn", method = RequestMethod.GET)
    public Result<SignInResult> login(String username, String password, String type, HttpServletRequest request) {
        try {
            if (StringUtils.isEmpty(type)) {
                throw new ValidException("请选择身份");
            }

            SignInResult result = null;
            switch (type) {
                case "webadmin":
                    result = userService.signIn(username, password, type);
                    break;
                case "tenants":
                    result = userService.signInTenantsUser(username, password, type);
                    break;
                case "integration":
//                    result = userService.signInIntegrationUser(username, password, type);
                    result = userService.ssoLogin(type, request);
                    break;
                default:
                    throw new ValidException("非法身份");
            }
            return ResultUtil.success("登录成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("登录失败：" + e.getMessage(), e);
        }
    }

    /**
     * 退出登录
     */
    @Auth("noauth")
    @GetMapping(value = "/signOut")
    public Result<String> signOut(HttpServletRequest request) throws Exception {
        request.logout();
        userService.singOut(request);
        return ResultUtil.success("");
    }

    @Auth("webadmin")
    @ApiOperation(value = "修改密码", notes = "修改密码")
    @RequestMapping(value = "/webadminUpdate", method = RequestMethod.PUT)
    public Result<String> webadminUpdate(HttpServletRequest request, @RequestBody UpdatePwd model) {
        try {
            this.userService.webadminUpdate(model.getId(), model.getPassword());
            return ResultUtil.success("修改成功");
        } catch (Exception e) {
            return ResultUtil.fail("修改失败：" + e.getMessage(), e);
        }
    }

    @Auth("integration")
    @ApiOperation(value = "项目管理员修改密码", notes = "项目管理员修改密码")
    @RequestMapping(value = "/integrationUserUpdate", method = RequestMethod.PUT)
    public Result<String> integrationUserUpdate(HttpServletRequest request, @RequestBody UpdatePwd model) {
        try {
            this.userService.integrationUserUpdate(model.getId(), model.getPassword());
            return ResultUtil.success("修改成功");
        } catch (Exception e) {
            return ResultUtil.fail("修改失败：" + e.getMessage(), e);
        }
    }

    @Auth("tenants")
    @ApiOperation(value = "租户修改密码", notes = "租户修改密码")
    @RequestMapping(value = "/tenantsUserUpdate", method = RequestMethod.PUT)
    public Result<String> tenantsUserUpdate(HttpServletRequest request, @RequestBody UpdatePwd model) {
        try {
            this.userService.tenantsUserUpdate(model.getId(), model.getPassword());
            return ResultUtil.success("修改成功");
        } catch (Exception e) {
            return ResultUtil.fail("修改失败：" + e.getMessage(), e);
        }
    }
}

@ApiModel(value = "UpdatePwd", description = "修改密码")
class UpdatePwd {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "密码")
    private String password;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
