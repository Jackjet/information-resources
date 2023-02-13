package d1.project.d1project.business.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.business.entity.DataSource;
import d1.project.d1project.business.model.datasource.*;
import d1.project.d1project.business.service.datasource.DataSourceService;
import d1.project.d1project.common.service.IOrganizationService;
import d1.project.d1project.system.entity.OrganizationEntity;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 数据源
 *
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/dataSourceManage")
public class DataSourceController {
    private final DataSourceService dataSourceService;
    private final IOrganizationService iOrganizationService;

    public DataSourceController(DataSourceService dataSourceService, IOrganizationService iOrganizationService) {
        this.dataSourceService = dataSourceService;
        this.iOrganizationService = iOrganizationService;
    }

    /**
     * 添加数据源
     */
    @PostMapping("/dataSourceInsert")
    public Result<String> dataSourceInsert(HttpServletRequest request, @Valid @RequestBody DataSourceManageDataSourceInsertPostVm params) {
        try {
            dataSourceService.insert(request, params);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 测试连接
     */
    @GetMapping("/connectTest")
    public Result<String> connectTest(@Valid DataSourceManageConnectTestGetVm params) {
        try {
            return ResultUtil.success("成功", dataSourceService.connectTest(params.getType(),params.getIp(),params.getPort(),params.getDataBaseName(),params.getServiceName(),params.getUsername(),params.getPassword()));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 修改数据源
     */
    @PutMapping("/dataSourceUpdate")
    public Result<String> dataSourceUpdate(@Valid @RequestBody DataSourceManageDataSourceUpdatePutVm params) {
        try {
            dataSourceService.update(params);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 查询数据源
     */
    @GetMapping("/getDataSource")
    public Result<Page<DataSource>> getDataSource(@Valid DataSourceManageGetDataSourceGetVm params) {
        try {
            return ResultUtil.success("成功", dataSourceService.findAll((JSONObject) JSON.toJSON(params)));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 查询数据源详情
     */
    @GetMapping("/getDataSourceById")
    public Result<DataSource> getDataSourceById(@Valid @NotBlank(message = "id不能为空") String id) {
        try {
            return ResultUtil.success("成功", dataSourceService.findById(id));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 删除数据源
     */
    @DeleteMapping("/deleteDataSourceById")
    public Result<String> deleteDataSourceById(@Valid @NotBlank(message = "id不能为空") String id) {
        try {
            dataSourceService.deleteById(id);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 停用或启用数据源
     */
    @PutMapping("/dataSourceEnableOrForbidden")
    public Result<String> dataSourceEnableOrForbidden(@Valid @RequestBody DataSourceManageEnableOrForbiddenPutVm params) {
        try {
            dataSourceService.dataSourceEnableOrForbidden(params.getId());
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 查询组织信息
     */
    @GetMapping("/getOrganizationOptions")
    public Result<List<OrganizationEntity>> getOrganizationOptions() {
        try {
            return ResultUtil.success("成功", iOrganizationService.getForest());
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }
}