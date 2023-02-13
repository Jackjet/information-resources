package d1.project.tangshan.operation.manage.entity;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Buter
 * @date 2020/3/20 14:40
 */
@Entity
@Table(name = "d1_base_environment_monitor_alarm")
@ApiModel(value = "BaseEnvironmentMonitorAlarmEntity", description = "基础环境监控预警")
public class BaseEnvironmentMonitorAlarmEntity extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("监控类型，基础环境(environment),系统(system)")
    private String type;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("节点id")
    private String nodeId;

    @ApiModelProperty("节点名称")
    private String nodeName;

    @ApiModelProperty("系统id")
    private String systemId;

    @ApiModelProperty("系统名称")
    private String systemName;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
}
