package d1.project.resourcesharingmgmt.resource.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

/**
 * 目录资源更新警告
 *
 * @author zhengyang
 */
@ApiModel(value = "WarningVm", description = "目录资源更新警告")
public class WarningVm extends PageableVm {
    @ApiModelProperty("信息资源代码")
    private String uviewNo;

    @ApiModelProperty("信息资源名称")
    private String uviewNm;

    @ApiModelProperty("信息资源提供方部门ID")
    private Integer orgId;

    @ApiModelProperty("更新周期")
    private String updateCyc;

    public String getUviewNo() {
        return uviewNo;
    }

    public void setUviewNo(String uviewNo) {
        this.uviewNo = uviewNo;
    }

    public String getUviewNm() {
        return uviewNm;
    }

    public void setUviewNm(String uviewNm) {
        this.uviewNm = uviewNm;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getUpdateCyc() {
        return updateCyc;
    }

    public void setUpdateCyc(String updateCyc) {
        this.updateCyc = updateCyc;
    }
}
