package d1.project.resource.resourcemanage.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resource.common.annotation.ApiAuth;
import d1.project.resource.resourcemanage.entity.DataSource;
import d1.project.resource.resourcemanage.entity.DataUnit;
import d1.project.resource.resourcemanage.entity.DataUnitField;
import d1.project.resource.resourcemanage.model.field.DataUnitFieldGetDataUnitFieldsGetVm;
import d1.project.resource.resourcemanage.model.field.DataUnitFieldUpdateDataUnitFieldsPutVm;
import d1.project.resource.resourcemanage.model.field.DataUnitFieldUpdateDataUnitFieldsReplacePutVm;
import d1.project.resource.resourcemanage.service.DataSourceService;
import d1.project.resource.resourcemanage.service.DataUnitFieldService;
import d1.project.resource.resourcemanage.service.DataUnitService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 元数据字段
 *
 */
@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/dataSourceInfo")
public class DataUnitFieldController {
    private final DataUnitFieldService dataUnitFieldService;
    private final DataUnitService dataUnitService;
    private final DataSourceService dataSourceService;

    public DataUnitFieldController(DataUnitFieldService dataUnitFieldService, DataUnitService dataUnitService, DataSourceService dataSourceService) {
        this.dataUnitFieldService = dataUnitFieldService;
        this.dataUnitService = dataUnitService;
        this.dataSourceService = dataSourceService;
    }

    /**
     * 更新元数据字段
     */
    @PutMapping("/updateDataUnitFields")
    public Result<String> updateDataUnitFields(@Valid @RequestBody DataUnitFieldUpdateDataUnitFieldsPutVm params) {
        try {
            DataSource dataSource = dataSourceService.findById(params.getDataSourceId());
            DataUnit dataUnit = dataUnitService.getDataUnitById(params.getDataUnitId());
            dataUnitFieldService.update(dataSource, dataUnit);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 查询元数据字段
     */
    @GetMapping("/getDataUnitFields")
    public Result<Page<DataUnitField>> getDataUnitFields(@Valid DataUnitFieldGetDataUnitFieldsGetVm params) {
        try {
            return ResultUtil.success("成功", dataUnitFieldService.findAll((JSONObject) JSON.toJSON(params)));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 修改元数据字段脱敏规则
     */
    @PutMapping("/updateFieldReplace")
    public Result<String> updateFieldReplace(@Valid @RequestBody DataUnitFieldUpdateDataUnitFieldsReplacePutVm params) {
        try {
            dataUnitFieldService.updateFieldReplace(params);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }
}