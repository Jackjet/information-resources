package d1.project.tangshan.operation.manage.entity;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Calendar;

/**
 * 管理页面端的用户entity
 *
 * @author Buter
 * @date 2020/3/15 18:05
 */

@Entity
@Table(name = "d1_web_admin_user")
@ApiModel(value = "WebAdminUser", description = "系统用户管理表")
public class WebAdminUser extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("名称")
    private String name;

    @Column(length = 100)
    @ApiModelProperty("密码")
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "注册时间", example = "2018-01-23 09:12:32")
    private Calendar signUpTime;


    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "最后登录时间", example = "2018-01-23 09:12:32")
    private Calendar lastSignInTime;

    @ApiModelProperty("状态 (0禁用1正常)")
    private String status;

    @ApiModelProperty("单位")
    private String organization;

    @ApiModelProperty("联系电话")
    private String tel;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("角色Id")
    private String roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("是否锁定账号预警")
    private String isWarn;

    @ApiModelProperty("是否邮箱预警")
    private String isEmile;

    @ApiModelProperty("是否短信预警")
    private String isSms;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Calendar getSignUpTime() {
        return signUpTime;
    }

    public void setSignUpTime(Calendar signUpTime) {
        this.signUpTime = signUpTime;
    }

    public Calendar getLastSignInTime() {
        return lastSignInTime;
    }

    public void setLastSignInTime(Calendar lastSignInTime) {
        this.lastSignInTime = lastSignInTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getIsWarn() {
        return isWarn;
    }

    public void setIsWarn(String isWarn) {
        this.isWarn = isWarn;
    }

    public String getIsEmile() {
        return isEmile;
    }

    public void setIsEmile(String isEmile) {
        this.isEmile = isEmile;
    }

    public String getIsSms() {
        return isSms;
    }

    public void setIsSms(String isSms) {
        this.isSms = isSms;
    }
}
