package d1.project.dcrun.center.webapi.datacache.entity;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author maoyy
 */
@Entity
@Table(name = "d1_key_config")
@ApiModel(value = "KeyConfig", description = "Key配置表")
public class KeyConfig extends BaseCreateAndUpdateEntity {
    @Column(length = 12)
    @ApiModelProperty("用户id")
    private String integrationId;
    @Column(length = 32)
    @ApiModelProperty("系统服务id")
    private String sysServiceId;
    @Column(length = 100)
    @ApiModelProperty("key")
    private String key;
    @ApiModelProperty("过期时间，秒，默认0(不限制)")
    private Integer expire;
    @Column(length = 100)
    @ApiModelProperty("appid")
    private String appid;
    @ApiModelProperty(value = "开发者名称")
    private String name;
    @Column(length = 100)
    @ApiModelProperty("权限，0:读、1:写、2:读+写")
    private String permission;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
