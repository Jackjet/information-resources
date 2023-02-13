package d1.project.api.integration.apimanage.entity;

import d1.framework.webapi.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "d1_source_api")
public class SourceApi extends BaseEntity {
    /**
     * 名称
     */
    private String name;
    /**
     * Host
     */
    private String host;

    /**
     * Path
     */
    private String path;
    /**
     * 请求协议
     */
    private String protocol;
    /**
     * 请求方式
     */
    private String method;
    /**
     * 内容格式类型
     */
    private String formatType;
    /**
     * body
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    private String body;
    /**
     * 入参内容
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    private String params;
    /**
     * 返回值
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    private String response;
    /**
     * 常量
     */
    @Lob
    @Column(columnDefinition = "TEXT")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    private String constants;
    /**
     * apiId
     */
    private String apiId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFormatType() {
        return formatType;
    }

    public void setFormatType(String formatType) {
        this.formatType = formatType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getConstants() {
        return constants;
    }

    public void setConstants(String constants) {
        this.constants = constants;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }
}
