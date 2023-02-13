package d1.project.d1project.business.model.datasource.group;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "DataSourceManageGroupInsertPostVm", description = "添加数据源组")
public class DataSourceManageGroupInsertPostVm {
    @NotBlank(message = "组名不能为空")
    @ApiModelProperty("组名")
    private String name;
    @ApiModelProperty("父节点id")
    private String parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
