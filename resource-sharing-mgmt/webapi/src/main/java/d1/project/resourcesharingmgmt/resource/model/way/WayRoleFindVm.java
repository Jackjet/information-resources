package d1.project.resourcesharingmgmt.resource.model.way;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author dy
 */
@ApiModel(value = "WayRoleFindVm", description = "代办/服务指引查询vm")
public class WayRoleFindVm {

    @ApiModelProperty("角色id")
    @NotBlank(message = "角色id不可为空")
    private String roleId;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
