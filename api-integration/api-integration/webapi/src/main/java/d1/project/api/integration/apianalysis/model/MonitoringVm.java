package d1.project.api.integration.apianalysis.model;

import java.util.List;

/**
 * @author baozh
 */
public class MonitoringVm {
    List<NameNumVm> apiFail;
    List<NameNumVm> apiTime;

    public List<NameNumVm> getApiFail() {
        return apiFail;
    }

    public void setApiFail(List<NameNumVm> apiFail) {
        this.apiFail = apiFail;
    }

    public List<NameNumVm> getApiTime() {
        return apiTime;
    }

    public void setApiTime(List<NameNumVm> apiTime) {
        this.apiTime = apiTime;
    }
}
