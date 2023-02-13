package d1.project.api.integration.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.project.api.integration.common.model.TimePageableVm;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 14:29
 */
@ApiModel(value = "WebAdminUserFindAllVm", description = "管理端_管理用户新增")
public class WebAdminUserFindAllVm extends TimePageableVm {

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("账号")
    private String account;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("组织名称")
    private String organizationName;

    @ApiModelProperty("性别")
    private String sex;


    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
