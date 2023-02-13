package d1.project.api.integration.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import java.util.List;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 21:33
 */
@ApiModel(value = "RoleUserInsertAllVm", description = "用户角色关联新增列表")
public class RoleUserInsertAllVm {

    @ApiModelProperty("角色id")
    private String roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("用户id")
    private List<String> userIds;

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

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }
}
