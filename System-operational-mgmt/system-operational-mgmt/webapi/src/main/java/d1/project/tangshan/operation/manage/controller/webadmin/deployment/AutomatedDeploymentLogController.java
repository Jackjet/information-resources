package d1.project.tangshan.operation.manage.controller.webadmin.deployment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.tangshan.operation.manage.entity.AutomatedDeploymentLogEntity;
import d1.project.tangshan.operation.manage.entity.BigDataApiEntity;
import d1.project.tangshan.operation.manage.service.AutomatedDeploymentLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Calendar;

/**
 * @Author chengh
 **/
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/deploymentLog")
@Api(value = "/webadmin/deploymentLog", tags = "自动化部署日志管理")
public class AutomatedDeploymentLogController {

    private final AutomatedDeploymentLogService automatedDeploymentLogService;

    @Autowired
    public AutomatedDeploymentLogController(AutomatedDeploymentLogService automatedDeploymentLogService) {
        this.automatedDeploymentLogService = automatedDeploymentLogService;
    }

    @ApiOperation(value = "查询", notes = "查询所有数据")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<AutomatedDeploymentLogEntity>> findAll(HttpServletRequest request, DeploymentLogFindAllVm model) {
        try {
            Page<AutomatedDeploymentLogEntity> result = automatedDeploymentLogService.findAll((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("查询成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("查询失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findByAutomatedDeploymentEntityId")
    public Result<AutomatedDeploymentLogEntity> findByAutomatedDeploymentEntityId(String id) {
        try {
            return ResultUtil.success("成功", automatedDeploymentLogService.findByAutomatedDeploymentEntityId(id));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }


    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<AutomatedDeploymentLogEntity> findById(String id) {
        try {
            return ResultUtil.success("成功", automatedDeploymentLogService.findById(id));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @Auth("noauth")
    @ApiOperation(value = "更新", notes = "更新数据")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(HttpServletRequest request, @Valid @RequestBody DeploymentLogUpdateVm model) {
        try {
            this.automatedDeploymentLogService.update((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail( e.getMessage(), e);
        }
    }

}

class DeploymentLogFindAllVm extends PageableVm {
    @ApiModelProperty("节点id")
    private String nodeId;
    @ApiModelProperty("系统id")
    private String sysId;
    @ApiModelProperty("开始时间")
    private String beginTime;
    @ApiModelProperty("结束时间")
    private String endTime;
    @ApiModelProperty("类型")
    private String type;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

class DeploymentLogUpdateVm {
    @NotBlank(message = "部署日志id不能为空")
    @ApiModelProperty("部署日志id")
    private String id;

    @ApiModelProperty("执行结果")
    private String result;
    @ApiModelProperty("执行日志")
    private String resultLog;

    public String getResultLog() {
        return resultLog;
    }

    public void setResultLog(String resultLog) {
        this.resultLog = resultLog;
    }

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
}

