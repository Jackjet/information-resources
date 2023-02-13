package d1.project.d1project.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

import javax.validation.constraints.NotBlank;

/**
 * 根据角色查找用户
 *
 * @author kikki
 * @date 2020-09-08 14:29
 */
@ApiModel(value = "RoleUserFindRoleIdVm", description = "根据角色查找用户")
public class RoleUserFindRoleIdVm extends PageableVm {

    @ApiModelProperty("姓名")
    private String userName;
    @ApiModelProperty("用户账号")
    private String userAccount;
    @ApiModelProperty("用户手机号")
    private String userPhone;
    @ApiModelProperty("组织机构名称")
    private String organizationName;
    @NotBlank(message = "角色id不可为空")
    private String roleId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}
