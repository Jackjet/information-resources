package d1.project.resourcesharingmgmt.resource.model.DemandedInfo;

import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author maoyuying
 */
public class DemandedInfoUpdateVm {
    @ApiModelProperty("id")
    @NotBlank(message = "id不可为空")
    private String id;

    @ApiModelProperty("受理状态,0未受理1已受理2已驳回")
    @NotBlank(message = "受理状态不可为空")
    private String status;

    @ApiModelProperty("受理意见")
    private String opinion;

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

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }
}
