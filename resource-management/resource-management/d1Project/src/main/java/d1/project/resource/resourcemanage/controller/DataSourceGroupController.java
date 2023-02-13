package d1.project.resource.resourcemanage.controller;

import com.alibaba.fastjson.JSONArray;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resource.common.annotation.ApiAuth;
import d1.project.resource.resourcemanage.entity.DataSourceGroup;
import d1.project.resource.resourcemanage.model.group.DataSourceManageGroupInsertPostVm;
import d1.project.resource.resourcemanage.model.group.DataSourceManageGroupUpdatePutVm;
import d1.project.resource.resourcemanage.service.DataSourceGroupService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 数据源分组
 *
 */
@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/dataSourceInfo")
public class DataSourceGroupController {
    private final DataSourceGroupService dataSourceGroupService;

    public DataSourceGroupController(DataSourceGroupService dataSourceGroupService) {
        this.dataSourceGroupService = dataSourceGroupService;
    }

    /**
     * 查询数据源组树
     */
    @GetMapping("/getDataSourceGroup")
    public Result<JSONArray> getDataSourceGroup() {
        try {
            return ResultUtil.success("成功", dataSourceGroupService.getDataSourceGroup());
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 查询数据源组列表
     */
    @GetMapping("/getDataSourceGroupList")
    public Result<List<DataSourceGroup>> getDataSourceGroupList() {
        try {
            return ResultUtil.success("成功", dataSourceGroupService.findAll());
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 添加数据源组
     */
    @PostMapping("/groupInsert")
    public Result<String> groupInsert(@Valid @RequestBody DataSourceManageGroupInsertPostVm params) {
        try {
            dataSourceGroupService.insert(params);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 修改数据源组
     */
    @PutMapping("/groupUpdate")
    public Result<String> groupUpdate(@Valid @RequestBody DataSourceManageGroupUpdatePutVm params) {
        try {
            dataSourceGroupService.update(params);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 删除数据源组
     */
    @DeleteMapping("/groupDelete")
    public Result<String> groupDelete(@Valid @NotBlank(message = "组id不能为空") String id) {
        try {
            dataSourceGroupService.delete(id);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }
}