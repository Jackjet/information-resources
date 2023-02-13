package d1.project.resource.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-09 10:01
 */
@ApiModel(value = "RoleMenuTreeInsertVm", description = "系统管理_角色权限_分配权限")
public class RoleMenuTreeInsertVm {

    @ApiModelProperty("角色id")
    @NotBlank(message = "角色id不可为空")
    private String roleId;

    @ApiModelProperty("菜单列表")
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
