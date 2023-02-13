package d1.project.resource.api.entity;

import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author libin
 * 源API
 */
@Entity
@Table(name = "d1_source_api")
public class SourceApi extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("名称")
    @Column(length = 50)
    private String name;

    @ApiModelProperty("分组ID")
    @Column(length = 50)
    private String groupId;

    @ApiModelProperty("Host")
    @Column(columnDefinition = "TEXT")
    private String host;

    @ApiModelProperty("是否支持负载均衡")
    private Boolean loadBalancing;

    @ApiModelProperty("Path")
    @Column(length = 100)
    private String path;

    @ApiModelProperty("请求协议")
    @Column(length = 10)
    private String protocol;

    @ApiModelProperty("请求方式(GET,POST,PUT,DELETE)")
    @Column(length = 20)
    private String method;

    @ApiModelProperty("内容格式类型(application/json,multipart/form-data,application/xml)")
    @Column(length = 30)
    private String formatType;

    @ApiModelProperty("body内容（除XML格式外以JSON格式存储）")
    @Lob
    @Column(columnDefinition = "TEXT")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    private String body;

    @ApiModelProperty("入参内容（除XML格式外以JSON格式存储）")
    @Lob
    @Column(columnDefinition = "TEXT")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    private String params;

    @ApiModelProperty("返回值（除XML格式外以JSON格式存储）")
    @Lob
    @Column(columnDefinition = "TEXT")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    private String response;

    @ApiModelProperty("常量")
    @Lob
    @Column(columnDefinition = "TEXT")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    private String constants;

    @ApiModelProperty("标签信息")
    @Column(columnDefinition = "TEXT")
    private String tagName;

    @ApiModelProperty("标签信息")
    @Column(columnDefinition = "TEXT")
    private String tagValue;

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

    public Boolean getLoadBalancing() {
        return loadBalancing;
    }

    public void setLoadBalancing(Boolean loadBalancing) {
        this.loadBalancing = loadBalancing;
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }
}
