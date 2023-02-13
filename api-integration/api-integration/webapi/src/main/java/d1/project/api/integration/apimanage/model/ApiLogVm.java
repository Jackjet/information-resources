package d1.project.api.integration.apimanage.model;

import d1.project.api.integration.common.utils.BaseUtils;

import java.util.Calendar;

/**
 * @author baozh
 */
public class ApiLogVm {
    private String id;

    private String method;

    private Calendar requestTime;

    private String responseCode;

    private Long responseTime;

    private String requestDay;

    public ApiLogVm(String id, String method, Calendar requestTime, String responseCode, Long responseTime) {
        this.id = id;
        this.method = method;
        this.requestTime = requestTime;
        this.responseCode = responseCode;
        this.responseTime = responseTime;
        this.requestDay = BaseUtils.calendarToString(requestTime);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getRequestDay() {
        return requestDay;
    }

    public void setRequestDay(String requestDay) {
        this.requestDay = requestDay;
    }
}
