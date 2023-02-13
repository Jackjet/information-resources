package d1.framework.permission.controller.webadmin;

import d1.framework.permission.DoBaseController;
import d1.framework.permission.annotation.Permission;
import d1.framework.permission.entity.Role;
import d1.framework.permission.impl.DoServiceImpBase;
import d1.framework.permission.model.RoleGetVm;
import d1.framework.permission.model.RolePostVm;
import d1.framework.permission.model.RolePutVm;
import d1.framework.permission.service.RoleService;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Auth("webadmin")
@Permission("nopermission")
@RestController
@RequestMapping(value = "/webadmin/permission/role")
@Api(value = "/webadmin/permission/role", tags = "角色管理")
public class RoleController extends DoBaseController<Role> {

    @Override
    protected DoServiceImpBase<Role> getBaseService() {
        return roleService;
    }


    @Autowired
    private RoleService roleService;

    @ApiOperation(value = "获取所有角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页,从1开始", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页几个，从1开始", dataType = "int")
    })
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<Role>> findAllWithPage(RoleGetVm model) {
        try {
            Page<Role> result = roleService.findAll(model);
            return ResultUtil.success("获取所有角色成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }

    }

    @ApiOperation(value = "创建角色")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result insert(@RequestBody RolePostVm model, HttpServletRequest request) {
        try {
            roleService.insert(model,request);

            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "更新角色")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result update(@RequestBody RolePutVm model, HttpServletRequest request) {
        try {
            roleService.update(model,request);
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "删除角色")
    @RequestMapping(value = "/deleteRole", method = RequestMethod.DELETE)
    public Result deleteRole(String id, HttpServletRequest request) {
        try {
            roleService.deleteUser(id,request);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }
}
