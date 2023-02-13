package d1.project.dcrun.center.webapi.dcapi.entity;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author lin
 */
@Entity
@Table(name = "d1_access_control_plan")
@ApiModel(value = "AccessControlPlan", description = "访问控制策略表")
public class AccessControlPlan extends BaseCreateAndUpdateEntity {
    @ApiModelProperty(value = "集成项目id")
    private String integrationId;
    @ApiModelProperty(value = "系统服务id")
    private String sysServiceId;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("限制类型(0：IP地址、1：账户名)")
    private String type;
    @ApiModelProperty("动作(0：允许、1：禁止)")
    private String operation;
    @Type(type = "org.hibernate.type.TextType")
    @Lob
    @ApiModelProperty("IP地址(为空时，不限制)")
    private String ips;
    @Type(type = "org.hibernate.type.TextType")
    @Lob
    @ApiModelProperty("账户名(为空时，不限制)")
    private String appIds;

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public String getSysServiceId() {
        return sysServiceId;
    }

    public void setSysServiceId(String sysServiceId) {
        this.sysServiceId = sysServiceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

    public String getAppIds() {
        return appIds;
    }

    public void setAppIds(String appIds) {
        this.appIds = appIds;
    }
}
