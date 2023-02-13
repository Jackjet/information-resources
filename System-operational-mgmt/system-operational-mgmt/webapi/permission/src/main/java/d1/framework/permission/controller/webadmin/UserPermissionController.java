package d1.framework.permission.controller.webadmin;

import d1.framework.permission.annotation.Permission;
import d1.framework.permission.model.UserPermissionPostVm;
import d1.framework.permission.model.UserPermissionSelectVm;
import d1.framework.permission.service.UserPermissionService;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Auth("webadmin")
@Permission("nopermission")
@RestController
@RequestMapping("/webadmin/permission/userPermission")
@Api(value = "/webadmin/permission/userPermission", tags = "用户权限管理")
public class UserPermissionController {
    @Autowired
    private UserPermissionService userPermissionService;

    @ApiOperation(value = "根据用户id，获取功能树和已勾选功能树")
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "String")
    @RequestMapping(value = "/findAll", method = {RequestMethod.GET})
    public Result<UserPermissionSelectVm> getMenuAndSelectByRoleId(String userId) {
        try {

            UserPermissionSelectVm userPerAndMenuTree = userPermissionService.getMenuAndSelectByUserId(userId);

            return ResultUtil.success("用户功能树获取成功", userPerAndMenuTree);
        } catch (Exception e) {
            return ResultUtil.fail( e.getMessage(),e);
        }
    }


    @ApiOperation(value = "根据用户id，批量更新权限")
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public Result insert(@RequestBody UserPermissionPostVm model) {
        try {
            userPermissionService.insert(model);

            return ResultUtil.success("权限更新成功");
        } catch (Exception e) {
            return ResultUtil.fail("权限更新失败:", e);
        }
    }
}
