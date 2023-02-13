package d1.project.resource.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.project.resource.system.model.WebAdminUserBase;

import javax.validation.constraints.NotBlank;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 14:29
 */
@ApiModel(value = "WebAdminUserInsertVm", description = "管理端_管理用户新增")
public class WebAdminUserInsertExcelVm extends WebAdminUserBase {

    @ApiModelProperty("账户")
    @NotBlank(message = "账户不可为空")
    private String account;
    private String roleName;
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
