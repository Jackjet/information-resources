package d1.project.resourcesharingmgmt.resource.model.way;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author dy
 */
@ApiModel(value = "WayGuideUpdateVm", description = "服务指引编辑vm")
public class WayGuideUpdateVm  extends WayGuideInsertVm {

    @ApiModelProperty("id")
    @NotBlank(message = "id不可为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
