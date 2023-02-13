package d1.project.resourcesharingmgmt.resource.model.AssetApiEx;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

/**
 * @author JungYoung
 */

@ApiModel(value = "AssetExLogFindAllVm", description = "挂接日志查询所有")
public class AssetExLogFindAllVm extends PageableVm {
    @ApiModelProperty("审批ID")
    private String assetExId;

    public String getAssetExId() {
        return assetExId;
    }

    public void setAssetExId(String assetExId) {
        this.assetExId = assetExId;
    }
}
