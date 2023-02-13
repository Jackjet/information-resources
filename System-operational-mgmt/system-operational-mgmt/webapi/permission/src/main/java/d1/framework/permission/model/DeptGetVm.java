package d1.framework.permission.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "DeptGetVm", description = "部门树")
public class DeptGetVm {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "名称")
    private String Name;
    @ApiModelProperty(value = "路径")
    private String path;
    @ApiModelProperty(value = "层级")
    private Integer level;
    @ApiModelProperty(value = "子集合")
    private List<DeptGetVm> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<DeptGetVm> getChildren() {
        return children;
    }

    public void setChildren(List<DeptGetVm> children) {
        this.children = children;
    }
}
