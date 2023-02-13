package d1.project.resourcesharingmgmt.resource.model.AssetApiEx;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

/**
 * @author zhengyang
 */

@ApiModel(value = "AssetDataExFindAllVm", description = "云数据查询所有")
public class AssetDataExFindAllVm extends PageableVm {
    @ApiModelProperty("资源目录ID,信息资源目录外键")
    private String uviewId;

    public String getUviewId() {
        return uviewId;
    }

    public void setUviewId(String uviewId) {
        this.uviewId = uviewId;
    }
}
