package d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo;

import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author zhengyang
 */
public class ResourceUseInfoAuditVm {
    @ApiModelProperty("id")
    @NotBlank(message = "id不可为空")
    private String id;

    @ApiModelProperty("审核状态,1已审核2已驳回")
    @NotBlank(message = "审核状态不可为空")
    private String status;

    @ApiModelProperty("审核说明,可为空，审批时填写")
    private String auditDesc;

    /**
     * 驳回原因
     */
    @ApiModelProperty("驳回原因")
    private String rejectDetail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRejectDetail() {
        return rejectDetail;
    }

    public void setRejectDetail(String rejectDetail) {
        this.rejectDetail = rejectDetail;
    }
}
