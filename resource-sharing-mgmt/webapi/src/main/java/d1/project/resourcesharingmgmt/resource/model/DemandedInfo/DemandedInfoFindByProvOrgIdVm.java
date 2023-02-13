package d1.project.resourcesharingmgmt.resource.model.DemandedInfo;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.project.resourcesharingmgmt.common.model.TimePageableVm;

/**
 * @author JungYoung
 */

@ApiModel(value = "DemandedInfoFindByProvOrgIdVm", description = "需求统计单位查询")
public class DemandedInfoFindByProvOrgIdVm extends TimePageableVm {
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
