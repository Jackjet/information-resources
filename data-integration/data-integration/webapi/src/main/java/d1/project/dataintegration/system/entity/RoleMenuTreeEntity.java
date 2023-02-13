package d1.project.dataintegration.system.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author 17503
 */
@Entity
@Table(name = "d1_role_menu_tree")
@ApiModel(value = "RoleMenuTreeEntity", description = "角色菜单关联")
public class RoleMenuTreeEntity extends BaseCreateEntity {

    @ApiModelProperty("角色id")
    @Column(length = 32)
    private String roleId;

    @ApiModelProperty("菜单id")
    @Column(length = 32)
    private String menuTreeId;

    public RoleMenuTreeEntity() {
    }

    public RoleMenuTreeEntity(String roleId, String menuTreeId) {
        this.roleId = roleId;
        this.menuTreeId = menuTreeId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getMenuTreeId() {
        return menuTreeId;
    }

    public void setMenuTreeId(String menuTreeId) {
        this.menuTreeId = menuTreeId;
    }
}
