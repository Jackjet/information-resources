package d1.framework.permission.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "UserRolePostVm", description = "用户添加角色")
public class UserRolePostVm {
    @ApiModelProperty(value = "用户Id")
    private String userId;

    @ApiModelProperty(value = "角色Id列表")
    private List<String> roleIds;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<String> roleIds) {
        this.roleIds = roleIds;
    }
}
