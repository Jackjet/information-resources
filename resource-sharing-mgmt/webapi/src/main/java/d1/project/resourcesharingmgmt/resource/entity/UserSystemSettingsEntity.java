package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "d1_user_system_settings")
@ApiModel(value = "UserSystemSettingsEntity", description = "用户系统访问设置")
public class UserSystemSettingsEntity extends BaseEntity {

    @ApiModelProperty("账户")
    @Column(length = 20)
    private String account;

    @ApiModelProperty("值")
    @Column(columnDefinition = "TEXT")
    private String value;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
