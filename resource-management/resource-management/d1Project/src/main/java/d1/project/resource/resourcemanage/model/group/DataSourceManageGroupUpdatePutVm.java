package d1.project.resource.resourcemanage.model.group;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "DataSourceManageGroupUpdatePutVm", description = "修改数据源组")
public class DataSourceManageGroupUpdatePutVm {
    @NotBlank(message = "组id不能为空")
    private String id;
    @NotBlank(message = "组名不能为空")
    @ApiModelProperty("组名")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
