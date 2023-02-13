package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.*;

/**
 * 资源使用日志表
 *
 * @author zhengyang
 */
@Entity
@Table(name = "d1_resource_use_log")
@ApiModel(value = "ResourceUseLogEntity", description = "资源使用日志表")
public class ResourceUseLogEntity extends BaseCreateEntity {
    /**
     * 资源ID
     */
    @ApiModelProperty("资源ID")
    private String resourceId;

    /**
     * 资源名称
     */
    @ApiModelProperty("资源名称")
    private String resourceName;

    /**
     * 审批人
     */
    @ApiModelProperty("审批人")
    private String createByName;

    /**
     * 审批环节
     */
    @ApiModelProperty("审批环节")
    private String processName;

    /**
     * 审核状态,1已审核2已驳回
     */
    @ApiModelProperty("审核状态,1已审核2已驳回")
    @Column(length = 4)
    private String status;

    @ApiModelProperty("审核说明,可为空，审批时填写")
    @Column(columnDefinition = "TEXT")
    private String auditDesc;

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuditDesc() {
        return auditDesc;
    }

    public void setAuditDesc(String auditDesc) {
        this.auditDesc = auditDesc;
    }
}
