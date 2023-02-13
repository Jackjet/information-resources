package d1.project.api.integration.application.entity;

import d1.project.api.integration.kong.entity.KongConsumer;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author libin
 * 普通用户
 */
@Entity
@Table(name = "d1_normal_consumer")
public class NormalConsumer extends KongConsumer {
    private String appid;
    private String ipPluginId;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getIpPluginId() {
        return ipPluginId;
    }

    public void setIpPluginId(String ipPluginId) {
        this.ipPluginId = ipPluginId;
    }
}
