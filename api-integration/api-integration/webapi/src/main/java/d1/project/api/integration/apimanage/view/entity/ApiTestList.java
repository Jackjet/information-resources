package d1.project.api.integration.apimanage.view.entity;

import d1.framework.webapi.annotation.ApiModelProperty;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Calendar;

@Entity
@Immutable
@Subselect("SELECT " +
        "d1_api_test_log.id as id," +
        "d1_api_test_log.api_id as api_id," +
        "d1_api_test_log.request_time as request_time," +
        "d1_api_test_log.response_code as response_code," +
        "d1_api_test_log.response_time as response_time " +
        "FROM d1_api_test_log")
public class ApiTestList {
    @Id
    private String id;

    @ApiModelProperty("API ID")
    private String apiId;

    @ApiModelProperty(value = "请求时间", example = "2018-01-01 01:01:01")
    private Calendar requestTime;

    @ApiModelProperty("响应状态码")
    private String responseCode;

    @ApiModelProperty("响应时长,单位为毫秒")
    private Long responseTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
}
