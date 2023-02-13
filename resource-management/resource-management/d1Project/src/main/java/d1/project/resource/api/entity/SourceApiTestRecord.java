package d1.project.resource.api.entity;

import d1.framework.webapi.entity.BaseEntity;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author libin
 * 测试记录
 */
@Entity
@Table(name = "d1_source_api_test_record")
public class SourceApiTestRecord extends BaseEntity {
    /**
     * 源APIId
     */
    @Column(length = 32)
    private String sourceApiId;

    /**
     * 方法 (GET/POST/PUT/DELETE)
     */
    @Column(length = 10)
    private String method;

    /**
     * 请求时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar requestTime;

    /**
     * 响应状态码
     */
    @Column(length = 5)
    private String responseCode;

    /**
     * 响应时长,单位为毫秒
     */
    @Column(length = 11)
    private Long responseTime;

    /**
     * 响应信息
     */
    @Column(columnDefinition = "TEXT")
    private String responseContent;

    /**
     * 请求信息
     */
    @Column(columnDefinition = "TEXT")
    private String requestContent;

    /**
     * 请求信息
     */
    @Column(length = 10)
    private String requestDay;

    public String getSourceApiId() {
        return sourceApiId;
    }

    public void setSourceApiId(String sourceApiId) {
        this.sourceApiId = sourceApiId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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

    public String getRequestDay() {
        return requestDay;
    }

    public void setRequestDay(String requestDay) {
        this.requestDay = requestDay;
    }
}
