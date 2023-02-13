package d1.project.resourcesharingmgmt.system.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 14:29
 */
@ApiModel(value = "WebAdminUserBaseVm", description = "管理端_管理用户基础")
public class WebAdminUserBase {

    @ApiModelProperty("姓名")
    @Length(message = "姓名长度超过100字符", min = 1, max = 100)
    private String name;

    @ApiModelProperty("头像")
    @Length(message = "头像长度超过100字符", min = 0, max = 500)
    private String avatar;

    @ApiModelProperty("手机号")
    @Length(message = "手机号长度超过100字符", min = 0, max = 100)
    private String phone;

    @ApiModelProperty("性别")
    private int sex;

    @ApiModelProperty("组织id")
    private String organizationId;

    @ApiModelProperty("角色id")
    private String roleId;

    @ApiModelProperty("电子邮箱")
    @Length(message = "电子邮箱长度超过100字符", min = 0, max = 100)
    private String email;

    @ApiModelProperty("备注")
    @Length(message = "备注长度超过100字符", min = 0, max = 2000)
    private String remark;

    @ApiModelProperty("身份证号")
    private String idCard;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }
}
