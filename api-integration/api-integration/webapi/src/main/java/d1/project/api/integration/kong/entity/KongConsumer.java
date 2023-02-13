package d1.project.api.integration.kong.entity;

import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseEntity;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.MappedSuperclass;

/**
 * kong consumer基础结构
 */
@MappedSuperclass
@DynamicUpdate
public class KongConsumer extends BaseEntity {
    @ApiModelProperty("consumerId")
    private String consumerId;
    @ApiModelProperty("用户")
    private String username;
    @ApiModelProperty("密钥")
    private String key;
    @ApiModelProperty("key-Auth密钥Id")
    private String keyAuthId;
    @ApiModelProperty("hmac密钥Id")
    private String hmacId;
    @ApiModelProperty("所属组")
    private String groups;
    @ApiModelProperty("所属网关")
    private String container;

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKeyAuthId() {
        return keyAuthId;
    }

    public void setKeyAuthId(String keyAuthId) {
        this.keyAuthId = keyAuthId;
    }

    public String getHmacId() {
        return hmacId;
    }

    public void setHmacId(String hmacId) {
        this.hmacId = hmacId;
    }

    public String getGroups() {
        return groups;
    }

    public void setGroups(String groups) {
        this.groups = groups;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }
}
