package d1.project.tangshan.operation.manage.controller.webadmin.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.tangshan.operation.manage.entity.BaseEnvironmentMonitorAlarmEntity;
import d1.project.tangshan.operation.manage.service.BaseEnvironmentMonitorAlarmService;
import d1.project.tangshan.operation.manage.utils.AlarmModule;
import io.swagger.annotations.Api;
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

/**
 * @Author chengh
 **/
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/environmentAlarm")
@Api(value = "/webadmin/environmentAlarm", tags = "基础环境监控设置预警")
public class BaseEnvironmentMonitorAlarmController {

    private final BaseEnvironmentMonitorAlarmService baseEnvironmentMonitorService;

    @Autowired
    public BaseEnvironmentMonitorAlarmController(BaseEnvironmentMonitorAlarmService baseEnvironmentMonitorService) {
        this.baseEnvironmentMonitorService = baseEnvironmentMonitorService;
    }

    @ApiOperation(value = "查询", notes = "查询所有数据")
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public Result<BaseEnvironmentMonitorAlarmEntity> find(HttpServletRequest request, AlarmFindVm model) {
        try {
            BaseEnvironmentMonitorAlarmEntity result = baseEnvironmentMonitorService.find((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("查询成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("查询失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "更新", notes = "更新数据")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(HttpServletRequest request, @Valid @RequestBody AlarmUpdateVm model) {
        try {
            this.baseEnvironmentMonitorService.update((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("设置预警成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查询", notes = "获取节点对应的基础环境监控预警")
    @RequestMapping(value = "/findByNodeId", method = RequestMethod.GET)
    public Result<String> findByNodeId(AlarmFindByNodeIdVm model) {
        try {
            BaseEnvironmentMonitorAlarmEntity result = null;
            if(AlarmModule.ENVIRONMENT.equals(model.getType())) {
                result = this.baseEnvironmentMonitorService.findByNodeIdAndType(model.getNodeId(),model.getType());
            } else {
                result = this.baseEnvironmentMonitorService.findByNodeIdAndSystemIdAndType(model.getNodeId(),model.getSystemId(), model.getType());
            }
            return ResultUtil.success("查询成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
}

class AlarmFindVm {
    @NotBlank(message = "监控类型不能为空")
    @ApiModelProperty("监控类型，基础环境(environment),系统(system)")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

class AlarmUpdateVm {
    @NotBlank(message = "监控类型不能为空")
    @ApiModelProperty("监控类型，基础环境(environment),系统(system)")
    private String type;
    @ApiModelProperty("节点Id")
    private String nodeId;
    @ApiModelProperty("节点名称")
    private String nodeName;
    @ApiModelProperty("系统Id")
    private String systemId;
    @ApiModelProperty("系统名称")
    private String systemName;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("邮箱")
    private String email;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

class AlarmFindByNodeIdVm {
    @NotBlank(message = "监控类型不能为空")
    @ApiModelProperty("监控类型，基础环境(environment),系统(system)")
    private String type;
    @NotBlank(message = "监控类型不能为空")
    @ApiModelProperty("节点Id")
    private String nodeId;
    @ApiModelProperty("系统Id")
    private String systemId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }
}
