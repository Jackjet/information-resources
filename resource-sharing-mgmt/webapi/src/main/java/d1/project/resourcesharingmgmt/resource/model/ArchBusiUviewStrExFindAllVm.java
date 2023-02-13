package d1.project.resourcesharingmgmt.resource.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

/**
 * @author JungYoung
 */

@ApiModel(value = "ArchBusiUviewStrExFindAllVm", description = "信息项查询所有")
public class ArchBusiUviewStrExFindAllVm extends PageableVm {
    @ApiModelProperty("资源目录ID,信息资源目录外键")
    private String uviewId;

    public String getUviewId() {
        return uviewId;
    }

    public void setUviewId(String uviewId) {
        this.uviewId = uviewId;
    }
}
