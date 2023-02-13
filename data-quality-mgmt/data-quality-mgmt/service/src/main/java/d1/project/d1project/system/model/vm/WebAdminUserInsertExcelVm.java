package d1.project.d1project.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.project.d1project.system.model.WebAdminUserBase;

import javax.validation.constraints.NotBlank;

/**
 * excel新增管理用户
 *
 * @author kikki
 * @date 2020-09-08 14:29
 */
@ApiModel(value = "WebAdminUserInsertExcelVm", description = "excel新增管理用户")
public class WebAdminUserInsertExcelVm extends WebAdminUserBase {

    @ApiModelProperty("账户")
    @NotBlank(message = "账户不可为空")
    private String account;
    @ApiModelProperty("角色名称")
    private String roleName;
    @ApiModelProperty("组织机构名称")
    private String organizationName;
    @ApiModelProperty("id")
    @NotBlank(message = "id不可为空")
    private String id;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }


}
