package d1.project.d1project.apidesign.model;

import d1.project.d1project.common.model.TimePageableVm;

public class ApiTestLogFindVm extends TimePageableVm {
    private String apiId;

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }
}