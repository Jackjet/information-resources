package d1.project.api.integration.apimanage.model;

import d1.project.api.integration.common.utils.BaseUtils;

import java.util.Calendar;

/**
 * @author baozh
 */
public class ApiTestCaseVm {
    private String id;
    private String method;
    private String ip;
    private Calendar createTime;
    private String time;

    public ApiTestCaseVm(String id, String method, String ip, Calendar createTime) {
        this.id = id;
        this.method = method;
        this.ip = ip;
        this.createTime = createTime;
        this.time = BaseUtils.calendarToString(createTime);
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
