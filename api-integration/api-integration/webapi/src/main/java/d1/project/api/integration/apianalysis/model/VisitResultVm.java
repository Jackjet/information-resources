package d1.project.api.integration.apianalysis.model;

/**
 * @author baozh
 */
public class VisitResultVm {
    private Long successNum;
    private Long failNum;
    private Long totalNum;

    public Long getSuccessNum() {
        if (successNum == null) {
            successNum = 0L;
        }
        return successNum;
    }

    public void setSuccessNum(Long successNum) {
        this.successNum = successNum;
    }

    public Long getFailNum() {
        if (failNum == null) {
            failNum = 0L;
        }
        return failNum;
    }

    public void setFailNum(Long failNum) {
        this.failNum = failNum;
    }

    public Long getTotalNum() {
        if (totalNum == null) {
            totalNum = 0L;
        }
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }
}
