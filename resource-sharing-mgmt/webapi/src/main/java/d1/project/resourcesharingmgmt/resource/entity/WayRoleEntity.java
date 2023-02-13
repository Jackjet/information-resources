package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author dy
 * way 包含代办模块、服务指引 下面检查代办模块
 */
@Entity
@Table(name = "d1_way_role")
@ApiModel(value = "WayRoleEntity", description = "角色分配关联表-代办模块、服务指引")
public class WayRoleEntity extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("代办模块Id")
    @Column(length = 32)
    private String wayId;

    @ApiModelProperty("代办模块名称")
    @Column(length = 200)
    private String wayName;

    @ApiModelProperty("类型（0代办、1服务指引）")
    @Column(length = 10)
    private String type;

    @ApiModelProperty("角色Id")
    @Column(length = 32)
    private String roleId;

    @ApiModelProperty("角色名称")
    @Column(length = 200)
    private String roleName;

    public String getWayId() {
        return wayId;
    }

    public void setWayId(String wayId) {
        this.wayId = wayId;
    }

    public String getWayName() {
        return wayName;
    }

    public void setWayName(String wayName) {
        this.wayName = wayName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
