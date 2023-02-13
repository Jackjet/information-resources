package d1.project.resource.common.controller;

import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resource.common.service.IDataSourceService;
import d1.project.resource.resourcemanage.entity.DataSource;
import d1.project.resource.resourcemanage.entity.DataUnit;
import d1.project.resource.resourcemanage.entity.DataUnitField;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 公用数据源接口
 */
//@Auth("webadmin")
@RestController
@RequestMapping("/common/dataSourceManage")
public class CommonDatasourceController {
    private final IDataSourceService iDataSourceService;

    public CommonDatasourceController(IDataSourceService iDataSourceService) {
        this.iDataSourceService = iDataSourceService;
    }

    /**
     * 查询数据源
     */
    @GetMapping("/findAll")
    public Result<DataSource> findAll(@Valid @NotBlank(message = "数据源类型不能为空") String type){
        try{
            return ResultUtil.success("成功",iDataSourceService.findAll(type));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 查询元数据
     */
    @GetMapping("/findAllByDataSourceId")
    public Result<DataUnit> findAllByDataSourceId(String dataSourceId){
        try{
            return ResultUtil.success("成功",iDataSourceService.findAllByDataSourceId(dataSourceId));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 查询元数据字段
     */
    @GetMapping("/findAllByDataUnitId")
    public Result<DataUnitField> findAllByDataUnitId(String dataUnitId){
        try{
            return ResultUtil.success("成功",iDataSourceService.findAllByDataUnitId(dataUnitId));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }
}
