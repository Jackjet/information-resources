package d1.project.d1project.system.entity;

import com.github.houbb.pinyin.util.PinyinHelper;
import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import d1.project.d1project.common.utils.BaseUtils;

import javax.persistence.*;
import java.util.Calendar;

/**
 * 系统用户管理表
 *
 * @author Buter
 * @date 2020/3/15 18:05
 */
@Entity
@Table(name = "d1_web_admin_user")
@ApiModel(value = "WebAdminUserEntity", description = "系统用户管理表")
public class WebAdminUserEntity extends BaseCreateAndUpdateEntity {

    @ApiModelProperty("账户")
    @Column(length = 100)
    private String account;

    @ApiModelProperty("单点登录id")
    @Column(length = 36)
    private String ssoId;

    @ApiModelProperty("头像")
    @Column(length = 500)
    private String avatar;

    @ApiModelProperty("拼音")
    private String pinyin;

    @ApiModelProperty("姓名")
    @Column(length = 100)
    private String name;

    @ApiModelProperty("手机号")
    @Column(length = 100)
    private String phone;

    @ApiModelProperty("最后登录时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar lastSignInTime;

    @ApiModelProperty("性别")
    @Column(length = 1)
    private int sex;

    @ApiModelProperty("密码")
    @Column(length = 32)
    private String password;

    @ApiModelProperty("电子邮箱")
    @Column(length = 100)
    private String email;

    @ApiModelProperty("身份证号")
    @Column(length = 30)
    private String idCard;

    @ApiModelProperty("启用")
    private boolean enable;

    @ApiModelProperty("组织名称")
    @Column(length = 100)
    private String organizationName;

    @ApiModelProperty("角色名称")
    @Column(length = 100)
    private String roleName;

    @ApiModelProperty("备注")
    @Column(length = 2000)
    private String remark;


    public void initInsert() {
        this.pinyin = PinyinHelper.toPinyin(this.name);
        this.enable = true;
        this.ssoId = "";
        this.roleName = "";
        this.organizationName = "";
        this.password = "e10adc3949ba59abbe56e057f20f883e";
        setId(BaseUtils.generate32Id());
        setCreateTime(Calendar.getInstance());
    }

    public String getSsoId() {
        return ssoId;
    }

    public void setSsoId(String ssoId) {
        this.ssoId = ssoId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
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

    public Calendar getLastSignInTime() {
        return lastSignInTime;
    }

    public void setLastSignInTime(Calendar lastSignInTime) {
        this.lastSignInTime = lastSignInTime;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
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
