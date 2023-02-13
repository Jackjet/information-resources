package d1.project.api.integration.apimanage.model;

import java.util.List;

/**
 * @author baozh
 */
public class ApiLogReturnVm {
    private Integer id;
    private String time;
    private List<ApiLogVm> logList;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<ApiLogVm> getLogList() {
        return logList;
    }

    public void setLogList(List<ApiLogVm> logList) {
        this.logList = logList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
