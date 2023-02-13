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
@Table(name = "d1_Api_RouteInfo")
@ApiModel(value = "ApiRouteInfo", description = "源API地址")
public class ApiRouteInfo extends BaseCreateAndUpdateEntity {
    @ApiModelProperty(value = "集成项目id")
    private String integrationId;
    @ApiModelProperty(value = "系统服务id")
    private String sysServiceId;
    @ApiModelProperty("访问控制策略id")
    private String accessStrategyId;
    @ApiModelProperty(value = "流量控制策略id")
    private String flowStrategyId;
    @ApiModelProperty(value = "api名称")
    private String name;
    @ApiModelProperty(value = "源api属性(路由属性)", notes = "GET 、PUT、 DELETE、 POST")
    private String sourceMethod;
    @ApiModelProperty(value = "源api地址(路由地址)", notes = "源路径")
    private String sourcePath;
    @ApiModelProperty(value = "目标api属性(service属性)", notes = "GET 、PUT、 DELETE、 POST")
    private String targetMethod;
    @ApiModelProperty(value = "目标api地址(service地址)", notes = "目标路径")
    private String targetPath;
    @ApiModelProperty(value = "备注")
    private String remark;

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

    public String getAccessStrategyId() {
        return accessStrategyId;
    }

    public void setAccessStrategyId(String accessStrategyId) {
        this.accessStrategyId = accessStrategyId;
    }

    public String getFlowStrategyId() {
        return flowStrategyId;
    }

    public void setFlowStrategyId(String flowStrategyId) {
        this.flowStrategyId = flowStrategyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceMethod() {
        return sourceMethod;
    }

    public void setSourceMethod(String sourceMethod) {
        this.sourceMethod = sourceMethod;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
