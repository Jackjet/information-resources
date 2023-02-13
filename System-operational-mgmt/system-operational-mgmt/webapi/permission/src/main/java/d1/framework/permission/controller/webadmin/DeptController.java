package d1.framework.permission.controller.webadmin;

import d1.framework.permission.DoBaseController;
import d1.framework.permission.annotation.Permission;
import d1.framework.permission.entity.Dept;
import d1.framework.permission.impl.DoServiceImpBase;
import d1.framework.permission.model.DeptGetVm;
import d1.framework.permission.model.DeptPostVm;
import d1.framework.permission.model.DeptPutVm;
import d1.framework.permission.service.DeptService;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Auth("webadmin")
@Permission("")
@RestController("webadmin")
@RequestMapping(value = "/webadmin/permission/dept")
@Api(value = "/webadmin/permission/dept", tags = "部门管理")
public class DeptController extends DoBaseController<Dept> {

    @Override
    protected DoServiceImpBase<Dept> getBaseService() {
        return deptService;
    }

    @Autowired
    private DeptService deptService;

    @ApiOperation(value = "获取全部部门树")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<List<DeptGetVm>> findAllDept() {
        try {
            List<DeptGetVm> result = deptService.findAllDept();
            return ResultUtil.success("获取功能树", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "创建数据")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result insert(@RequestBody DeptPostVm model) {
        try {
            deptService.insert(model);

            return ResultUtil.success("请求成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "更新数据")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result update(@RequestBody DeptPutVm model) {
        try {
            deptService.update(model);

            return ResultUtil.success("请求成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

}
