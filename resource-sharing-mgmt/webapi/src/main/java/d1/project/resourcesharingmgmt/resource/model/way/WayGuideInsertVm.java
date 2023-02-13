package d1.project.resourcesharingmgmt.resource.model.way;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author dy
 */
@ApiModel(value = "WayGuideInsertVm", description = "服务指引添加vm")
public class WayGuideInsertVm {
    @ApiModelProperty("文件名称")
    @NotBlank(message = "文件名称不可为空")
    private String name;

    @ApiModelProperty("路径")
    @NotBlank(message = "路径不可为空")
    private String path;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
