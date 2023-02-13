package d1.project.tangshan.operation.manage.controller.webadmin.operations.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.tangshan.operation.manage.entity.operations.log.SystemLog;
import d1.project.tangshan.operation.manage.entity.operations.log.SystemLog;
import d1.project.tangshan.operation.manage.service.operations.log.SystemLogService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Calendar;

/**
 * @author lin
 */
@Auth("webadmin")
@RestController("/webadmin/log/systemLog")
@RequestMapping("/webadmin/log/systemLog")
@Api(value = "/webadmin/log/systemLog", tags = "系统日志管理")
public class SystemLogController {
    private final SystemLogService systemLogService;

    public SystemLogController(SystemLogService systemLogService) {
        this.systemLogService = systemLogService;
    }
    @Auth("noauth")
    @ApiOperation(value = "新增")
    @PostMapping(value = "/insertLog")
    public Result<String> insertLog(@Valid @RequestBody SystemLogInsertVm model) {
        try {
            SystemLog systemLog = MyUtils.model2Entity(model, SystemLog.class);
            systemLog.setId(MyUtils.generate32Id());
            systemLog.setCreateTime(Calendar.getInstance());
            systemLogService.insert(systemLog);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
    
    @ApiOperation(value = "系统日志列表（分页）")
    @GetMapping(value = "/findAll")
    public Result<Page<SystemLog>> findAll(SystemLogFindAllVm model) {
        try {
            Page<SystemLog> result = systemLogService.findAll((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
}

@ApiModel(value = "SystemLogFindAllVm", description = "查询")
class SystemLogFindAllVm extends PageableVm {
    @ApiModelProperty("事件源")
    private String source;
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
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
@ApiModel(value = "SystemLogInsertVm", description = "新增")
class SystemLogInsertVm {
    @ApiModelProperty("事件源")
    private String source;
    @ApiModelProperty("事件信息")
    private String info;
    @ApiModelProperty("事件结果 0失败1成功")
    private String result;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}