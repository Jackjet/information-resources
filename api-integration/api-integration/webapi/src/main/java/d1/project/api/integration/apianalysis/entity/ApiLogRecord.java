package d1.project.api.integration.apianalysis.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseEntity;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author baozh
 */

@Entity
@Table(name = "d1_api_log_record")
@ApiModel(value = "ApiLogRecord", description = "API访问日志记录")
public class ApiLogRecord extends BaseEntity {

    @ApiModelProperty("应用ID")
    @Column(length = 32)
    private String appId;

    @ApiModelProperty("Api ID")
    @Column(length = 32)
    private String apiId;

    @ApiModelProperty("客户端IP/请求IP")
    @Column(length = 50)
    private String requestIp;

    @ApiModelProperty("请求方式(PUT/POST/GET/DELETE)")
    @Column(length = 10)
    private String requestMethod;

    @ApiModelProperty("请求时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar requestTime;

    @ApiModelProperty("响应时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar responseTime;

    @ApiModelProperty("响应状态码")
    @Column(length = 10)
    private String responseCode;

    @ApiModelProperty("响应时长")
    @Column(length = 10)
    private Long duration;

    @ApiModelProperty("响应时长分类(0:小于100ms;1:100ms-1s;2:1-10s;3:10-100s;4:大于100s)")
    @Column(length = 1)
    private Integer durationType;

    @ApiModelProperty("状态")
    @Column(length = 10)
    private Integer status;

    @ApiModelProperty("请求内容")
    @Column(columnDefinition = "TEXT")
    private String requestContent;

    @ApiModelProperty("响应内容")
    @Column(columnDefinition = "TEXT")
    private String responseContent;

    @ApiModelProperty("所属容器")
    private String container;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getRequestIp() {
        return requestIp;
    }

    public void setRequestIp(String requestIp) {
        this.requestIp = requestIp;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public Calendar getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Calendar requestTime) {
        this.requestTime = requestTime;
    }

    public Calendar getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Calendar responseTime) {
        this.responseTime = responseTime;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Integer getDurationType() {
        return durationType;
    }

    public void setDurationType(Integer durationType) {
        this.durationType = durationType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public void setRequestContent(String requestContent) {
        this.requestContent = requestContent;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }
}
