package d1.project.d1project.apidesign.model;

import d1.framework.webapi.model.PageableVm;

public class ApiDesignedFindVm extends PageableVm {
    private String name;
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}