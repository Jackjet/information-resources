package d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;
import d1.project.resourcesharingmgmt.common.model.TimePageableVm;

/**
 * @author JungYoung
 */

@ApiModel(value = "ResourceUseInfoFindByProvOrgIdVm", description = "云接口申请单位查询")
public class ResourceUseInfoFindByProvOrgIdVm extends TimePageableVm {
    /**
     * 申请部门id
     */
    @ApiModelProperty("申请部门id")
    private String createDeptId;

    public String getCreateDeptId() {
        return createDeptId;
    }

    public void setCreateDeptId(String createDeptId) {
        this.createDeptId = createDeptId;
    }
}
