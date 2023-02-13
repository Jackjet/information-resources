package d1.project.resourcesharingmgmt.resource.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * 我的待办-需求使用申请
 *
 * @author zhengyang
 */
@ApiModel(value = "ResourceUseInfoAnalysisVm", description = "我的待办-需求使用申请")
public class ResourceUseInfoAnalysisVm {
    @ApiModelProperty("申请数")
    private long resourceUse;

    @ApiModelProperty("目录资源新增待审批")
    private long resourceUseNotApproval;

    public long getResourceUse() {
        return resourceUse;
    }

    public void setResourceUse(long resourceUse) {
        this.resourceUse = resourceUse;
    }

    public long getResourceUseNotApproval() {
        return resourceUseNotApproval;
    }

    public void setResourceUseNotApproval(long resourceUseNotApproval) {
        this.resourceUseNotApproval = resourceUseNotApproval;
    }

    public ResourceUseInfoAnalysisVm() {
    }

    public ResourceUseInfoAnalysisVm(long resourceUse, long resourceUseNotApproval) {
        this.resourceUse = resourceUse;
        this.resourceUseNotApproval = resourceUseNotApproval;
    }
}
