package d1.project.dcrun.center.webapi.dcapi.entity.view;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * 设备钥匙授权记录左连接设备授权结果
 *
 * @author Kikki 2019/11/5 17:29
 */
@Entity
@Immutable
@Subselect("SELECT model.*,access.name as access_strategy_name,flow.name as flow_strategy_name " +
        "FROM d1_api_route_info model LEFT JOIN d1_access_control_plan access " +
        "on model.access_strategy_id = access.id LEFT JOIN d1_flow_control_plan flow " +
        "on flow.id = model.flow_strategy_id")
public class ApiView {
    @Id
    private String id;

    @ApiModelProperty(value = "集成项目id")
    private String integrationId;

    @ApiModelProperty(value = "系统服务id")
    private String sysServiceId;

    @ApiModelProperty("访问控制策略id")
    private String accessStrategyId;

    @ApiModelProperty("访问控制策略名称")
    private String accessStrategyName;

    @ApiModelProperty(value = "流量控制策略id")
    private String flowStrategyId;

    @ApiModelProperty(value = "流量控制策略名称")
    private String flowStrategyName;

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

    @ApiModelProperty("更新人id")
    private String updateById;

    @ApiModelProperty("更新时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @ApiModelProperty("创建人id")
    private String createById;

    @ApiModelProperty("创建时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getAccessStrategyName() {
        return accessStrategyName;
    }

    public void setAccessStrategyName(String accessStrategyName) {
        this.accessStrategyName = accessStrategyName;
    }

    public String getFlowStrategyId() {
        return flowStrategyId;
    }

    public void setFlowStrategyId(String flowStrategyId) {
        this.flowStrategyId = flowStrategyId;
    }

    public String getFlowStrategyName() {
        return flowStrategyName;
    }

    public void setFlowStrategyName(String flowStrategyName) {
        this.flowStrategyName = flowStrategyName;
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

    public String getUpdateById() {
        return updateById;
    }

    public void setUpdateById(String updateById) {
        this.updateById = updateById;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
