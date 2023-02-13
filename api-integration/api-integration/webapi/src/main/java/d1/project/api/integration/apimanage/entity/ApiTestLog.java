package d1.project.api.integration.apimanage.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseEntity;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author baozh
 */

@Entity
@Table(name = "d1_api_test_log")
@ApiModel(value = "ApiTestLog", description = "API测试记录")
public class ApiTestLog extends BaseEntity {

    @ApiModelProperty("API ID")
    @Column(length = 32)
    private String apiId;

    @ApiModelProperty("方法 (GET/POST/PUT/DELETE)")
    @Column(length = 10)
    private String method;

    @ApiModelProperty(value = "请求时间", example = "2018-01-01 01:01:01")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar requestTime;

    @ApiModelProperty("响应状态码")
    @Column(length = 5)
    private String responseCode;

    @ApiModelProperty("响应时长,单位为毫秒")
    @Column(length = 11)
    private Long responseTime;

    @ApiModelProperty("响应信息")
    @Column(columnDefinition = "TEXT")
    private String responseContent;

    @ApiModelProperty("请求信息")
    @Column(columnDefinition = "TEXT")
    private String requestContent;

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public Calendar getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Calendar requestTime) {
        this.requestTime = requestTime;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public Long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Long responseTime) {
        this.responseTime = responseTime;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public String getRequestContent() {
        return requestContent;
    }

    public void setRequestContent(String requestContent) {
        this.requestContent = requestContent;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
