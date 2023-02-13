package d1.framework.permission.controller.webadmin;


import d1.framework.permission.annotation.Permission;
import d1.framework.permission.model.UserRolePostVm;
import d1.framework.permission.model.UserRoleSelectVm;
import d1.framework.permission.service.UserRoleService;
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

/**
 * @author all
 */
@Auth("webadmin")
@Permission("nopermission")
@RestController
@RequestMapping(value = "/webadmin/permission/userRole")
@Api(value = "/webadmin/permission/userRole", tags = "用户角色管理")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @ApiOperation(value = "根据用户Id获取角色列表和已勾选角色")
    @ApiImplicitParam(value = "用户id", name = "userId", dataType = "String")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result getRolesAndSelectByUserId(String userId) {
        try {
            UserRoleSelectVm res = userRoleService.getRolesAndSelectByUserId(userId);
            return ResultUtil.success("角色列表获取成功", res);
        } catch (Exception e) {
            return ResultUtil.fail("角色列表获取成功：消息" + e.getMessage());
        }
    }


    @ApiOperation(value = "用户添加/更新角色列表")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result insert(@RequestBody UserRolePostVm model) {
        try {
            userRoleService.insert(model);

            return ResultUtil.success("角色列表更新成功");
        } catch (Exception e) {
            return ResultUtil.fail("角色列表更新失败" + e.getMessage());
        }
    }
}
