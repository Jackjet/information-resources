package d1.framework.permission.controller.base;

import d1.framework.permission.DoBaseController;
import d1.framework.permission.entity.Dept;
import d1.framework.permission.impl.DoServiceImpBase;
import d1.framework.permission.service.DeptService;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController("base")
@RequestMapping(value = "/base/dept")
@Api(value = "/base/dept", tags = "部门管理")
public class DeptController extends DoBaseController<Dept> {
    @Override
    protected DoServiceImpBase<Dept> getBaseService() {
        return deptService;
    }

    @Autowired
    private DeptService deptService;

    @ApiOperation(value = "获取全部部门")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<List<Dept>> findAllDept() {
        try {
            List<Dept> result = deptService.findAll();
            return ResultUtil.success("获取全部部门", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }
}
