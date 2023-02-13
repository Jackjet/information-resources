package d1.project.tangshan.operation.manage.controller.webadmin.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.tangshan.operation.manage.Constants;
import d1.project.tangshan.operation.manage.model.PageableVm;
import d1.project.tangshan.operation.manage.model.TimePageableVm;
import d1.project.tangshan.operation.manage.service.BaseEnvironmentMonitorAlarmService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * @Author chengh
 **/
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/dataMonitor")
@Api(value = "/webadmin/dataMonitor", tags = "数据监控")
public class DataMonitorController {

    private final BaseEnvironmentMonitorAlarmService baseEnvironmentMonitorService;

    @Autowired
    public DataMonitorController(BaseEnvironmentMonitorAlarmService baseEnvironmentMonitorService) {
        this.baseEnvironmentMonitorService = baseEnvironmentMonitorService;
    }

    @ApiOperation(value = "节点状态分析")
    @GetMapping(value = "/nodeStatusAnalysis")
    public Result<JSONObject> nodeStatusAnalysis(DataMonitorControllerNodeStatusAnalysisVm model) {
        try {
            return ResultUtil.success(Constants.SUCCESS, baseEnvironmentMonitorService.nodeStatusAnalysis((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "系统状态分析")
    @GetMapping(value = "/systemStatusAnalysis")
    public Result<JSONObject> systemStatusAnalysis(DataMonitorControllerSystemStatusAnalysisVm model) {
        try {
            return ResultUtil.success(Constants.SUCCESS, baseEnvironmentMonitorService.systemStatusAnalysis((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

}


class DataMonitorControllerNodeStatusAnalysisVm extends PageableVm {
    @NotBlank(message = "开始时间不能为空")
    @ApiModelProperty(value = "开始时间")
    private String beginTime;
    @NotBlank(message = "结束时间不能为空")
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty("节点")
    private String nodeName;

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}

class DataMonitorControllerSystemStatusAnalysisVm extends PageableVm{
    @ApiModelProperty(value = "开始时间")
    private String beginTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty("节点名称")
    private String nodeName;
    @ApiModelProperty("系统名称")
    private String systemName;

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }
}
