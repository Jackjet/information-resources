package d1.project.resourcesharingmgmt.resource.model.way;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

/**
 * @author dy
 */
@ApiModel(value = "WayGuideFindAllVm", description = "服务指引查询vm")
public class WayGuideFindAllVm extends PageableVm {

    @ApiModelProperty("文件名称")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
