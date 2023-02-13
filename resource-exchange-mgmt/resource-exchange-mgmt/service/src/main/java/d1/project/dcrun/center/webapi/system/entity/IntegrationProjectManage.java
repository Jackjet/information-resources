package d1.project.dcrun.center.webapi.system.entity;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author libin
 */
@Entity
@Table(name = "d1_integration_project_manage")
@ApiModel(value = "IntegrationProjectManage", description = "用户管理")
public class IntegrationProjectManage extends BaseCreateAndUpdateEntity {
    @ApiModelProperty(value = "租户id")
    private String tenantId;
    @ApiModelProperty(value = "管理员账号")
    private String code;
    @ApiModelProperty(value = "密码")
    private String password;
    @ApiModelProperty(value = "用户appid")
    private String appid;
    @ApiModelProperty(value = "用户appkey")
    private String appkey;
    @ApiModelProperty(value = "备注")
    private String remark;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
