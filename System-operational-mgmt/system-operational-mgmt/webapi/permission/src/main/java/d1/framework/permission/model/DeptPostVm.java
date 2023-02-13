package d1.framework.permission.model;

import io.swagger.annotations.ApiModelProperty;

public class DeptPostVm {
    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "上级Id")
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
