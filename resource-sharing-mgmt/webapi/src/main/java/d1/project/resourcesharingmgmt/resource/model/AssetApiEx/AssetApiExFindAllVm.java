package d1.project.resourcesharingmgmt.resource.model.AssetApiEx;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

import javax.validation.constraints.NotBlank;

/**
 * @author JungYoung
 */

@ApiModel(value = "AssetApiExFindAllVm", description = "云接口查询所有")
public class AssetApiExFindAllVm extends PageableVm {
    @ApiModelProperty("资源目录ID,信息资源目录外键")
    private String uviewId;
    @ApiModelProperty("api名称")
    private String name;
    @ApiModelProperty("groupId名称")
    private String groupId;

    public String getUviewId() {
        return uviewId;
    }

    public void setUviewId(String uviewId) {
        this.uviewId = uviewId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
