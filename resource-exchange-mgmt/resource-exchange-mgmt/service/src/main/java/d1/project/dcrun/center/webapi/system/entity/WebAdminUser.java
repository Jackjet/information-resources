package d1.project.dcrun.center.webapi.system.entity;

import d1.framework.webapi.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author chengh
 * 用户管理
 */
@Entity
@Table(name = "d1_web_admin_user")
@ApiModel(value = "WebAdminUser", description = "系统用户管理表")
public class WebAdminUser extends BaseEntity {

    @Column(length = 20)
    @ApiModelProperty("名称")
    private String name;

    @Column(length = 100)
    @ApiModelProperty("密码")
    private String password;

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
}
