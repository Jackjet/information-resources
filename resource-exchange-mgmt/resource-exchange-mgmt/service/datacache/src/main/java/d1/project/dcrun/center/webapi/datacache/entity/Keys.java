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
@Table(name = "d1_keys")
@ApiModel(value = "Keys", description = "Key定义表")
public class Keys extends BaseCreateAndUpdateEntity {
    @Column(length = 12)
    @ApiModelProperty("用户id")
    private String integrationId;

    @Column(length = 32)
    @ApiModelProperty("系统服务id")
    private String sysServiceId;

    @Column(length = 100)
    @ApiModelProperty("key")
    private String key;

    @Column(columnDefinition = "TEXT")
    @ApiModelProperty("value说明")
    private String value;

    @ApiModelProperty("过期时间，秒，默认0(不限制")
    private Integer expire;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }
}
