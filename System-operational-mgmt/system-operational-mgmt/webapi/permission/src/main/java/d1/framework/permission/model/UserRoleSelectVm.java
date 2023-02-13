package d1.framework.permission.model;

import d1.framework.permission.entity.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "UserRoleSelectVm", description = "用户选择角色返回结果")
public class UserRoleSelectVm {
    @ApiModelProperty(value = "角色列表")
    private List<Role> roles;

    @ApiModelProperty(value = "已选择列表")
    private List<String> choose;

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<String> getChoose() {
        return choose;
    }

    public void setChoose(List<String> choose) {
        this.choose = choose;
    }
}
