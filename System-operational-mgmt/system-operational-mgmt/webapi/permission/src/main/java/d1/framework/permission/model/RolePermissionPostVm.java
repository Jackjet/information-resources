package d1.framework.permission.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "RolePermissionPostVm", description = "添加角色权限的model")
public class RolePermissionPostVm {
    @ApiModelProperty(value = "角色id")
    private String roleId;
    @ApiModelProperty(value = "菜单id")
    private List<String> menuTreeIds;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<String> getMenuTreeIds() {
        return menuTreeIds;
    }

    public void setMenuTreeIds(List<String> menuTreeIds) {
        this.menuTreeIds = menuTreeIds;
    }
}
