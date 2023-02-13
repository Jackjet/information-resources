package d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo;

import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * @author maoyuying
 */
public class ResourceUseLogFindAllVm {
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
     * 审核状态,1已审核2已驳回
     */
    @ApiModelProperty("审核状态,1已审核2已驳回")
    private String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
