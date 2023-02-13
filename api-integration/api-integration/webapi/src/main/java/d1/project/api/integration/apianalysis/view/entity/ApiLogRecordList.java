package d1.project.api.integration.apianalysis.view.entity;

import d1.framework.webapi.annotation.ApiModelProperty;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Calendar;

/**
 * @author baozh
 */

@Entity
@Immutable
@Subselect("SELECT " +
        "d1_api_log_record.id AS id," +
        "d1_api_log_record.app_id AS app_id," +
        "d1_api_log_record.api_id AS api_id," +
        "d1_application.name AS app_name," +
        "d1_api_base_info.name AS api_name," +
        "d1_api_log_record.request_method AS method," +
        "d1_api_log_record.request_time AS request_time," +
        "d1_api_log_record.response_code AS response_code," +
        "d1_api_log_record.response_time AS response_time " +
        "FROM d1_api_log_record " +
        "LEFT JOIN d1_application " +
        "ON d1_application.id = d1_api_log_record.app_id " +
        "LEFT JOIN d1_api_base_info " +
        "ON d1_api_base_info.id = d1_api_log_record.api_id")
public class ApiLogRecordList {
    @Id
    private String id;
    @ApiModelProperty("应用Id")
    private String appId;
    @ApiModelProperty("应用名称")
    private String appName;
    @ApiModelProperty("api Id")
    private String apiId;
    @ApiModelProperty("api名称")
    private String apiName;
    @ApiModelProperty("方法")
    private String method;
    @ApiModelProperty("请求时间")
    private Calendar requestTime;
    @ApiModelProperty("响应状态")
    private String responseCode;
    @ApiModelProperty("响应时间")
    private Calendar responseTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
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

    public Calendar getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Calendar responseTime) {
        this.responseTime = responseTime;
    }
}
