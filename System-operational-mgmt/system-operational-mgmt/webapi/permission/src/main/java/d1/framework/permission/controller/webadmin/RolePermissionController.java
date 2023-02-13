package d1.framework.permission.controller.webadmin;

import d1.framework.permission.annotation.Permission;
import d1.framework.permission.model.RolePermissionPostVm;
import d1.framework.permission.model.RolePermissionSelectVm;
import d1.framework.permission.service.RolePermissionService;
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

import javax.servlet.http.HttpServletRequest;

@Auth("webadmin")
@Permission("nopermission")
@RestController
@RequestMapping("/webadmin/permission/rolePermission")
@Api(value = "/webadmin/permission/rolePermission", tags = "角色权限管理")
public class RolePermissionController {

    @Autowired
    private RolePermissionService rolePermissionService;

    @ApiOperation(value = "根据角色id，获取功能树和已勾选功能树")
    @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "String")
    @RequestMapping(value = "/findAll", method = {RequestMethod.GET})
    public Result<RolePermissionSelectVm> getMenuAndSelectByRoleId(String roleId) {
        try {

            RolePermissionSelectVm rolePerAndMenuTree = rolePermissionService.getMenuAndSelectByRoleId(roleId);
            return ResultUtil.success("角色功能树获取成功", rolePerAndMenuTree);
        } catch (Exception e) {
            return ResultUtil.fail("角色功能树获取失败：" + e.getMessage());
        }
    }


    @ApiOperation(value = "根据角色id，批量更新权限")
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public Result insert(@RequestBody RolePermissionPostVm model, HttpServletRequest request) {
        try {
            rolePermissionService.insert(model,request);
            return ResultUtil.success("分配权限成功");
        } catch (Exception e) {
            return ResultUtil.fail("分配权限失败:"+e.getMessage(), e);
        }
    }


}
