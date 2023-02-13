package d1.project.d1project.apidesign.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * API设计信息
 *
 * @author baozh
 */

@Entity
@Table(name = "d1_api_design")
@ApiModel(value = "ApiDesign", description = "API设计信息")
public class ApiDesign extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("名称")
    @Column(length = 50)
    private String name;

    @ApiModelProperty("请求路径")
    @Column(length = 255)
    private String requestUrl;

    @ApiModelProperty("描述")
    @Column(columnDefinition = "TEXT")
    private String detail;

    @ApiModelProperty("状态(0,待编排；1，待部署,2，已部署)")
    @Column(length = 3)
    private String status;

    @ApiModelProperty("创建人名称")
    @Column(length = 50)
    private String createName;

    @ApiModelProperty("修改人名称")
    @Column(length = 50)
    private String updateName;

    @ApiModelProperty("协议(Http/Https)")
    @Column(length = 10)
    private String protocol;

    @ApiModelProperty("分组ID")
    @Column(length = 40)
    private String groupId;

    @ApiModelProperty("请求方式(GET/POST/PUT/DELETE)")
    @Column(length = 10)
    private String method;

    @ApiModelProperty("params参数")
    @Column(columnDefinition = "TEXT")
    private String params;

    @ApiModelProperty("body格式(JSON/form-data)")
    @Column(length = 50)
    private String formatType;

    @ApiModelProperty("body参数")
    @Column(columnDefinition = "TEXT")
    private String body;

    @ApiModelProperty("返回参数")
    @Column(columnDefinition = "TEXT")
    private String response;

    @ApiModelProperty("标签KEY列表")
    @Column(columnDefinition = "TEXT")
    private String metaKey;

    @ApiModelProperty("标签Value列表")
    @Column(columnDefinition = "TEXT")
    private String metaValue;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
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

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getFormatType() {
        return formatType;
    }

    public void setFormatType(String formatType) {
        this.formatType = formatType;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getMetaKey() {
        return metaKey;
    }

    public void setMetaKey(String metaKey) {
        this.metaKey = metaKey;
    }

    public String getMetaValue() {
        return metaValue;
    }

    public void setMetaValue(String metaValue) {
        this.metaValue = metaValue;
    }
}
