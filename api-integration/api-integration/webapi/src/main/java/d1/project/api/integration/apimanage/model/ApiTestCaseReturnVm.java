package d1.project.api.integration.apimanage.model;

import java.util.List;

/**
 * @author baozh
 */
public class ApiTestCaseReturnVm {
    private Integer id;
    private String time;
    private List<ApiTestCaseVm> caseList;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<ApiTestCaseVm> getCaseList() {
        return caseList;
    }

    public void setCaseList(List<ApiTestCaseVm> caseList) {
        this.caseList = caseList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
