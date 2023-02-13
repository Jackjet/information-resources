package d1.project.resourcesharingmgmt.resource.model.correction;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.project.resourcesharingmgmt.common.model.TimePageableVm;

import javax.persistence.Column;

/**
 * @author zhengyang
 */

@ApiModel(value = "CorrectionFindAllVm", description = "纠错分页查询")
public class CorrectionFindAllVm extends TimePageableVm {
    /**
     * 提交部门ID
     */
    private String orgId;

    /**
     * 纠错部门ID
     */
    @Column(length = 32)
    private String correctionOrgId;

    /**
     * 描述，0未处理，1已处理，2已驳回
     */
    private Integer status;

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getCorrectionOrgId() {
        return correctionOrgId;
    }

    public void setCorrectionOrgId(String correctionOrgId) {
        this.correctionOrgId = correctionOrgId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
