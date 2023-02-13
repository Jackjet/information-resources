package d1.project.dcrun.center.webapi.common.service.sys.user;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author maoyy
 */
@Entity
@Table(name = "d1_sys_user")
@ApiModel(value = "SysUser", description = "系统服务开发者管理")
public class SysUser extends BaseCreateAndUpdateEntity {
    @ApiModelProperty(value = "集成项目id")
    private String integrationId;
    @ApiModelProperty(value = "开发者id")
    private String appid;
    @ApiModelProperty(value = "开发者密钥")
    private String appkey;
    @ApiModelProperty(value = "开发者名称")
    private String name;
    @ApiModelProperty(value = "备注")
    private String remark;

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
