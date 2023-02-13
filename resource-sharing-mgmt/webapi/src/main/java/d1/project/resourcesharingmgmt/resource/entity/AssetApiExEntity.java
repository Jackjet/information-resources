package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 信息资源目录与云数据关联表
 *
 * @author zhengyang
 */
@Entity
@Table(name = "d1_asset_api_ex")
@ApiModel(value = "AssetApiExEntity", description = "信息资源目录与云数据关联表")
public class AssetApiExEntity extends BaseCreateEntity {
    @ApiModelProperty("信息资源ID")
    @Column(length = 32)
    private String uviewId;

    @ApiModelProperty("信息资源名称")
    @Column(length = 225)
    private String uviewNm;

    @ApiModelProperty("云数据ID")
    @Column(length = 32)
    private String sourceApiId;

    @ApiModelProperty("名称")
    @Column(length = 100)
    private String name;

    @ApiModelProperty("Host,JSON字符串")
    @Column(columnDefinition = "TEXT")
    private String host;

    @ApiModelProperty("是否支持负载均衡")
    private boolean loadBalancing;

    @ApiModelProperty("分组Id")
    @Column(length = 50)
    private String groupId;

    @ApiModelProperty("Path")
    @Column(length = 200)
    private String path;

    @ApiModelProperty("请求协议(Http/Https)")
    @Column(length = 10)
    private String protocol;

    @ApiModelProperty("请求方式,GET,POST,PUT,DELETE")
    @Column(length = 10)
    private String method;

    @ApiModelProperty("内容格式类型,JSON、form-data、XML")
    @Column(length = 50)
    private String formatType;

    @ApiModelProperty("body")
    @Column(columnDefinition = "TEXT")
    private String body;

    @ApiModelProperty("入参内容")
    @Column(columnDefinition = "TEXT")
    private String params;

    @ApiModelProperty("返回参数")
    @Column(columnDefinition = "TEXT")
    private String response;

    @ApiModelProperty("常量")
    @Column(columnDefinition = "TEXT")
    private String constants;

    @ApiModelProperty("标签信息")
    @Column(columnDefinition = "TEXT")
    private String tagName;

    @ApiModelProperty("标签信息")
    @Column(columnDefinition = "TEXT")
    private String tagValue;

    @ApiModelProperty("云数据描述")
    @Column(columnDefinition = "TEXT")
    private String detail;

    public String getUviewId() {
        return uviewId;
    }

    public void setUviewId(String uviewId) {
        this.uviewId = uviewId;
    }

    public String getUviewNm() {
        return uviewNm;
    }

    public void setUviewNm(String uviewNm) {
        this.uviewNm = uviewNm;
    }

    public String getSourceApiId() {
        return sourceApiId;
    }

    public void setSourceApiId(String sourceApiId) {
        this.sourceApiId = sourceApiId;
    }

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

    public boolean isLoadBalancing() {
        return loadBalancing;
    }

    public void setLoadBalancing(boolean loadBalancing) {
        this.loadBalancing = loadBalancing;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }


}
