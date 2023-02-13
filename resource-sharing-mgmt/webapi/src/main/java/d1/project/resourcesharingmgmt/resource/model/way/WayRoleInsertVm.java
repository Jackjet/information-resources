package d1.project.resourcesharingmgmt.resource.model.way;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author dy
 */
@ApiModel(value = "WayRoleInsertVm", description = "代办/服务指引 保存vm")
public class WayRoleInsertVm {
    @ApiModelProperty("角色Id")
    @NotBlank(message = "角色id不可为空")
    private String roleId;

    @ApiModelProperty("代办Id列表")
    private List<String> wayIds;

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public List<String> getWayIds() {
        return wayIds;
    }

    public void setWayIds(List<String> wayIds) {
        this.wayIds = wayIds;
    }
}
