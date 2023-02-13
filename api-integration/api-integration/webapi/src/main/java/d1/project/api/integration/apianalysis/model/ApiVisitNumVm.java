package d1.project.api.integration.apianalysis.model;

import java.util.List;

/**
 * @author baozh
 */
public class ApiVisitNumVm {
    private List<DateNumVm> totalNum;
    private List<DateNumVm> failNum;

    public List<DateNumVm> getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(List<DateNumVm> totalNum) {
        this.totalNum = totalNum;
    }

    public List<DateNumVm> getFailNum() {
        return failNum;
    }

    public void setFailNum(List<DateNumVm> failNum) {
        this.failNum = failNum;
    }
}
