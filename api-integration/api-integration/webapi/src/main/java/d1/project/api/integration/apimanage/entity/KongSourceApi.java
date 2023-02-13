package d1.project.api.integration.apimanage.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author libin
 */
@Entity
@Table(name = "d1_kong_source_api")
@ApiModel(value = "KongSourceApi", description = "源API在kong中的配置")
public class KongSourceApi  extends BaseEntity {
    /**
     * Host
     */
    private String host;
    /**
     * Path
     */
    private String path;
    /**
     * 请求方式
     */
    private String method;

    /**
     * kong upstreamId
     */
    private String upstreamId;
    /**
     * kong serviceId
     */
    private String serviceId;
    /**
     * kong upstreamName
     */
    private String upstreamName;

    /**
     * 请求协议
     */
    private String protocol;

    /**
     * 源ApiId
     */
    private String sourceId;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUpstreamId() {
        return upstreamId;
    }

    public void setUpstreamId(String upstreamId) {
        this.upstreamId = upstreamId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getUpstreamName() {
        return upstreamName;
    }

    public void setUpstreamName(String upstreamName) {
        this.upstreamName = upstreamName;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
}
