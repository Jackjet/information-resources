package d1.project.resourcesharingmgmt.resource.model.AssetApiEx;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import java.util.Calendar;

/**
 * @author JungYoung
 */

@ApiModel(value = "AssetApiExVm", description = "云接口详情")
public class AssetApiExVm{
    @ApiModelProperty("信息资源ID")
    private String id;

    @ApiModelProperty("信息资源ID")
    private String uviewId;

    @ApiModelProperty("信息资源名称")
    private String uviewNm;

    @ApiModelProperty("云数据ID")
    private String sourceApiId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("Host,JSON字符串")
    private String host;

    @ApiModelProperty("是否支持负载均衡")
    private boolean loadBalancing;

    @ApiModelProperty("分组Id")
    private String groupId;

    @ApiModelProperty("Path")
    private String path;

    @ApiModelProperty("请求协议(Http/Https)")
    private String protocol;

    @ApiModelProperty("请求方式,GET,POST,PUT,DELETE")
    private String method;

    @ApiModelProperty("内容格式类型,JSON、form-data、XML")
    private String formatType;

    @ApiModelProperty("body")
    private String body;

    @ApiModelProperty("入参内容")
    private String params;

    @ApiModelProperty("返回参数")
    private String response;

    @ApiModelProperty("常量")
    private String constants;

    @ApiModelProperty("标签信息")
    private String tagName;

    @ApiModelProperty("标签信息")
    private String tagValue;

    @ApiModelProperty("云数据描述")
    private String detail;

    /**
     * 容器
     */
    private String container;

    /**
     * 路由信息
     */
    private String routeInfo;

    private String createById;

    private Calendar createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getRouteInfo() {
        return routeInfo;
    }

    public void setRouteInfo(String routeInfo) {
        this.routeInfo = routeInfo;
    }

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }
}
