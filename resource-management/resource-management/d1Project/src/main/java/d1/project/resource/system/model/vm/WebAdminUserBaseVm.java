package d1.project.resource.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseEntity;
import org.hibernate.validator.constraints.Length;

/**
 * MY_JAR_NAME
 *
 * @author kikki
 * @date 2020-11-22 20:00
 */
@ApiModel(value = "WebAdminUserBaseVm", description = "用户基础列表")
public class WebAdminUserBaseVm extends BaseEntity {

    @ApiModelProperty("姓名")
    @Length(message = "姓名长度超过100字符", min = 1, max = 100)
    private String name;

    @ApiModelProperty("手机号")
    @Length(message = "手机号长度超过100字符", min = 0, max = 100)
    private String phone;

    @ApiModelProperty("账户")
    private String account;

    @ApiModelProperty("组织名称")
    private String organizationName;

    @ApiModelProperty("角色名称")
    private String roleName;

    public WebAdminUserBaseVm(String id, String name, String phone, String account, String organizationName, String roleName) {
        super.setId(id);
        this.name = name;
        this.phone = phone;
        this.account = account;
        this.organizationName = organizationName;
        this.roleName = roleName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
