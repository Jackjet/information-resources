package d1.project.tangshan.operation.manage.controller.webadmin.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.tangshan.operation.manage.model.ModuleCountVm;
import d1.project.tangshan.operation.manage.service.BaseEnvironmentMonitorAlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Author chengh
 **/
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/moduleAccess")
@Api(value = "/webadmin/moduleAccess", tags = "模块访问统计")
public class ModuleAccessController {

    private final BaseEnvironmentMonitorAlarmService baseEnvironmentMonitorService;

    @Autowired
    public ModuleAccessController(BaseEnvironmentMonitorAlarmService baseEnvironmentMonitorService) {
        this.baseEnvironmentMonitorService = baseEnvironmentMonitorService;
    }

    @ApiOperation(value = "模块访问统计")
    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public Result<Page<ModuleCountVm>> count(CountVm model) {
        try {
            Page<ModuleCountVm> result = baseEnvironmentMonitorService.count((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("查询成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("查询失败：" + e.getMessage(), e);
        }
    }


}

class CountVm extends PageableVm {
    @ApiModelProperty("系统名称")
    private String name;
    @ApiModelProperty("模块名称")
    private String moduleName;
    @NotBlank(message = "统计单位不能为空")
    @ApiModelProperty("统计单位，时(hour)日(day),月(month),周(week),默认是日")
    private String unit;
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}



