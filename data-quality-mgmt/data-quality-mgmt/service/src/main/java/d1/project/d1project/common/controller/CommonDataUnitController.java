package d1.project.d1project.common.controller;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.business.entity.DataUnit;
import d1.project.d1project.common.service.IDataUnitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 通用接口_元数据
 *
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/common/dataUnit")
public class CommonDataUnitController {
    private final IDataUnitService iDataUnitService;

    public CommonDataUnitController(IDataUnitService iDataUnitService) {
        this.iDataUnitService = iDataUnitService;
    }

    /**
     * 查询元数据
     */
    @GetMapping("/findAllByDataSourceId")
    public Result<List<DataUnit>> findAll(@Valid @NotBlank(message = "数据源id不能为空") String dataSourceId){
        try{
            return ResultUtil.success("成功", iDataUnitService.findAllByDataSourceId(dataSourceId));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }
}
