package d1.framework.permission.controller.webadmin;

import d1.framework.permission.annotation.Permission;
import d1.framework.permission.model.DeptPermissionPostVm;
import d1.framework.permission.model.DeptPermissionSelectVm;
import d1.framework.permission.model.RolePermissionSelectVm;
import d1.framework.permission.service.DeptPermissionService;
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
@Permission("")
@RestController
@RequestMapping("/webadmin/permission/deptPermission")
@Api(value = "/webadmin/permission/deptPermission", tags = "部门权限管理")
public class DeptPermissionController {
    @Autowired
    private DeptPermissionService deptPermissionService;

    @ApiOperation(value = "根据部门id，获取功能树和已勾选功能树")
    @ApiImplicitParam(name = "deptId", value = "部门ID", required = true, dataType = "String")
    @RequestMapping(value = "/findAll", method = {RequestMethod.GET})
    public Result<RolePermissionSelectVm> getMenuAndSelectByDeptId(String deptId) {
        try {

            DeptPermissionSelectVm rolePerAndMenuTree = deptPermissionService.getMenuAndSelectByDeptId(deptId);

            return ResultUtil.success("树和角色权限列表", rolePerAndMenuTree);
        } catch (Exception e) {
            return ResultUtil.fail( e.getMessage(),e);
        }
    }


    @ApiOperation(value = "根据部门id，批量更新权限")
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public Result insert(@RequestBody DeptPermissionPostVm model) {
        try {
            deptPermissionService.insert(model);

            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(),e);
        }
    }
}
