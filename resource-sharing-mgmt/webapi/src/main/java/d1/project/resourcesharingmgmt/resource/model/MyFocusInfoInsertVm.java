package d1.project.resourcesharingmgmt.resource.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author JungYoung
 */
@ApiModel(value = "MyFocusInfoInsertVm", description = "添加我的收藏")
public class MyFocusInfoInsertVm {
    @ApiModelProperty("信息资源ID")
    @NotBlank(message = "信息资源ID不可为空")
    private String uviewId;

    public String getUviewId() {
        return uviewId;
    }

    public void setUviewId(String uviewId) {
        this.uviewId = uviewId;
    }
}
