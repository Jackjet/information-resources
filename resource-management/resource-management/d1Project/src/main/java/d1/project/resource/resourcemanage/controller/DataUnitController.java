package d1.project.resource.resourcemanage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resource.common.annotation.ApiAuth;
import d1.project.resource.resourcemanage.entity.DataSource;
import d1.project.resource.resourcemanage.entity.DataUnit;
import d1.project.resource.resourcemanage.model.unit.DataUnitGetDataUnitGetVm;
import d1.project.resource.resourcemanage.model.unit.DataUnitUpdateDataUnitPutVm;
import d1.project.resource.resourcemanage.service.DataSourceService;
import d1.project.resource.resourcemanage.service.DataUnitFieldService;
import d1.project.resource.resourcemanage.service.DataUnitService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 元数据
 *
 */
@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/dataSourceInfo")
public class DataUnitController {
    private final DataUnitService dataUnitService;
    private final DataSourceService dataSourceService;
    private final DataUnitFieldService dataUnitFieldService;

    public DataUnitController(DataUnitService dataUnitService, DataSourceService dataSourceService, DataUnitFieldService dataUnitFieldService) {
        this.dataUnitService = dataUnitService;
        this.dataSourceService = dataSourceService;
        this.dataUnitFieldService = dataUnitFieldService;
    }

    /**
     * 查询元数据
     */
    @GetMapping("/getDataUnit")
    public Result<Page<DataUnit>> getDataUnit(@Valid DataUnitGetDataUnitGetVm params) {
        try {
            return ResultUtil.success("成功", dataUnitService.getDataUnit((JSONObject) JSON.toJSON(params)));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 更新元数据
     */
    @PutMapping("/updateDataUnit")
    @Transactional(rollbackOn = Exception.class)
    public Result<String> updateDataUnit(HttpServletRequest request, @Valid @RequestBody DataUnitUpdateDataUnitPutVm params) {
        try {
            DataSource dataSource = dataSourceService.findById(params.getDataSourceId());
            if(!dataSourceService.connectTest(dataSource.getType(),dataSource.getIp(),dataSource.getPort(),dataSource.getDataBaseName(),
                    dataSource.getServiceName(),dataSource.getUsername(),dataSource.getPassword())) {
                throw new DoValidException("当前数据源连接失败");
            }

            dataUnitFieldService.deleteBySourceId(params.getDataSourceId());
            dataUnitService.updateDataUnit(request, dataSource);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 查询元数据详情
     */
    @GetMapping("/getDataUnitById")
    public Result<DataUnit> getDataUnitById(@Valid @NotBlank(message = "id不能为空") String id) {
        try {
            return ResultUtil.success("成功", dataUnitService.getDataUnitById(id));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }
}