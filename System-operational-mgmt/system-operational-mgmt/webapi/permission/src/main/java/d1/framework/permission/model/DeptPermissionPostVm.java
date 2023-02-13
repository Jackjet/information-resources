package d1.framework.permission.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;


@ApiModel(value = "DeptPermissionPostVm", description = "添加部門权限的model")
public class DeptPermissionPostVm {
    @ApiModelProperty(value = "角色id")
    private String deptId;
    @ApiModelProperty(value = "菜单id")
    private List<String> menuTreeIds;

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public List<String> getMenuTreeIds() {
        return menuTreeIds;
    }

    public void setMenuTreeIds(List<String> menuTreeIds) {
        this.menuTreeIds = menuTreeIds;
    }
}
