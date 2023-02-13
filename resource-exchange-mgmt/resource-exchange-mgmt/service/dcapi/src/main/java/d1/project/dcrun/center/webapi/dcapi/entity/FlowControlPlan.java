package d1.project.dcrun.center.webapi.dcapi.entity;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author lin
 */
@Entity
@Table(name = "d1_flow_control_plan")
@ApiModel(value = "FlowControlPlan", description = "流量控制策略表")
public class FlowControlPlan extends BaseCreateAndUpdateEntity {
    @ApiModelProperty(value = "集成项目id")
    private String integrationId;
    @ApiModelProperty(value = "系统服务id")
    private String sysServiceId;
    @ApiModelProperty("策略名称")
    private String name;
    @ApiModelProperty("流量限制类型(0：无限制、1：账户名 2：IP地址)")
    private String type;
    @ApiModelProperty(value = "时长", notes = "流量限制时长。与API流量限制和用户流量限制配合使用,以天为单位")
    private Integer time;
    @ApiModelProperty("API流量限制")
    private Integer apiFlowLimit;
    @ApiModelProperty(value = "用户流量限制", notes = "不超过API流量限制，与流量限制时长配合使用，某些账户的访问次数")
    private Integer appIdFlowLimit;
    @ApiModelProperty(value = "源IP流量限制", notes = "不超过API流量限制，与流量限制时长配合使用，某些IP的访问次数")
    private Integer ipFlowLimit;
    @ApiModelProperty("描述")
    private String description;

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public String getSysServiceId() {
        return sysServiceId;
    }

    public void setSysServiceId(String sysServiceId) {
        this.sysServiceId = sysServiceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getApiFlowLimit() {
        return apiFlowLimit;
    }

    public void setApiFlowLimit(Integer apiFlowLimit) {
        this.apiFlowLimit = apiFlowLimit;
    }

    public Integer getAppIdFlowLimit() {
        return appIdFlowLimit;
    }

    public void setAppIdFlowLimit(Integer appIdFlowLimit) {
        this.appIdFlowLimit = appIdFlowLimit;
    }

    public Integer getIpFlowLimit() {
        return ipFlowLimit;
    }

    public void setIpFlowLimit(Integer ipFlowLimit) {
        this.ipFlowLimit = ipFlowLimit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
