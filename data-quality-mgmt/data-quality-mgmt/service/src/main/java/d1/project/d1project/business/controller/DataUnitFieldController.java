package d1.project.d1project.business.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.business.entity.DataSource;
import d1.project.d1project.business.entity.DataUnit;
import d1.project.d1project.business.entity.DataUnitField;
import d1.project.d1project.business.model.datasource.field.DataUnitFieldGetDataUnitFieldsGetVm;
import d1.project.d1project.business.model.datasource.field.DataUnitFieldUpdateDataUnitFieldsPutVm;
import d1.project.d1project.business.service.datasource.DataSourceService;
import d1.project.d1project.business.service.datasource.DataUnitFieldService;
import d1.project.d1project.business.service.datasource.DataUnitService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 元数据字段
 *
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/dataSourceManage")
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
}