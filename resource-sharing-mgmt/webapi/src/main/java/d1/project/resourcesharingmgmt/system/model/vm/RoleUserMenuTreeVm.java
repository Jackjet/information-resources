package d1.project.resourcesharingmgmt.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.project.resourcesharingmgmt.system.entity.RoleUserEntity;
import d1.project.resourcesharingmgmt.system.model.MenuTreeTree;

import java.util.List;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-12 22:42
 */
@ApiModel("角色用户菜单")
public class RoleUserMenuTreeVm {

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色Id")
    private String roleId;

    @ApiModelProperty("菜单树")
    private List<MenuTreeTree> menuTree;

    public RoleUserMenuTreeVm() {
    }

    public RoleUserMenuTreeVm(RoleUserEntity roleUser, List<MenuTreeTree> menuTree) {
        this.roleName = roleUser.getRoleName();
        this.roleId = roleUser.getRoleId();
        this.menuTree = menuTree;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<MenuTreeTree> getMenuTree() {
        return menuTree;
    }

    public void setMenuTree(List<MenuTreeTree> menuTree) {
        this.menuTree = menuTree;
    }
}
