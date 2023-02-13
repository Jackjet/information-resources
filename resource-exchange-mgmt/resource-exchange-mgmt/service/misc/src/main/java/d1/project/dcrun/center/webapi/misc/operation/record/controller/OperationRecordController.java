package d1.project.dcrun.center.webapi.misc.operation.record.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.misc.operation.record.service.OperationRecordReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author maoyy
 */

@RestController
@RequestMapping("/service/operationRecord")
@Api(value = "/service/operationRecord", description = "上报服务操作记录")
public class OperationRecordController {
    @Autowired
    private OperationRecordReportService operationRecordReportService;

    @ApiOperation(value = "上报服务操作记录")
    @RequestMapping(value = "/report", method = RequestMethod.POST)
    public Result report(@Valid @RequestBody OperationRecordReportVm model, HttpServletRequest request) {
        try {
            //TODO 后期加上认证，现在appkey的值是appid对应的key值
            operationRecordReportService.update((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("上报服务操作记录成功");
        } catch (Exception e) {
            return ResultUtil.fail("上报服务操作记录失败" + e.getMessage(), e);
        }
    }
}

@ApiModel(value = "OperationRecordReportVm", description = "上报服务操作记录vm")
class OperationRecordReportVm {
    @NotBlank(message = "操作日志id不能为空")
    @ApiModelProperty(value = "操作日志id")
    private String id;
    @NotBlank(message = "操作结果不能为空")
    @ApiModelProperty(value = "操作结果")
    private String result;
    @NotNull(message = "启动时间不能为空")
    @ApiModelProperty(value = "启动时间")
    private long startTime;
    @NotNull(message = "结束时间不能为空")
    @ApiModelProperty(value = "结束时间")
    private long endTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
}
