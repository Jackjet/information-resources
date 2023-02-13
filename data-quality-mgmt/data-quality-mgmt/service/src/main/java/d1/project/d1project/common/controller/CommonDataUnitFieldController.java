package d1.project.d1project.common.controller;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.business.entity.DataUnitField;
import d1.project.d1project.common.service.IDataUnitFieldService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 通用接口_元数据字段
 *
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/common/dataUnitField")
public class CommonDataUnitFieldController {
    private final IDataUnitFieldService iDataUnitFieldService;

    public CommonDataUnitFieldController(IDataUnitFieldService iDataUnitFieldService) {
        this.iDataUnitFieldService = iDataUnitFieldService;
    }

    /**
     * 根据元数据id查询元数据字段
     */
    @GetMapping("/findAllByDataUnitId")
    public Result<List<DataUnitField>> findAllByDataUnitId(@Valid @NotBlank(message = "元数据id不能为空") String dataUnitId){
        try{
            return ResultUtil.success("成功", iDataUnitFieldService.findAllByDataUnitId(dataUnitId));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }
}
