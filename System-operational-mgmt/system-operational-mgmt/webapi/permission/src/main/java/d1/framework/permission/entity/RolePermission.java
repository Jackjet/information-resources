package d1.framework.permission.entity;

import d1.framework.webapi.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author all
 */
@Entity
@Table(name = "d1_role_permission")
@ApiModel(value = "RolePermission", description = "角色权限表")
public class RolePermission extends BaseEntity {

    @ApiModelProperty(value = "角色ID")
    private String roleId;

    @ApiModelProperty(value = "功能树Id")
    private String menuTreeId;

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
