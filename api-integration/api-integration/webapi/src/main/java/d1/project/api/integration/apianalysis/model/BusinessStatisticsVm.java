package d1.project.api.integration.apianalysis.model;

/**
 * @author baozh
 */
public class BusinessStatisticsVm {
    private Long todayNum = 0L;
    private Double successRate = 0.0;
    private Long apiNum = 0L;
    private Long authNum = 0L;
    private Long appNum = 0L;

    public Long getTodayNum() {
        return todayNum;
    }

    public void setTodayNum(Long todayNum) {
        this.todayNum = todayNum;
    }

    public Double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(Double successRate) {
        this.successRate = successRate;
    }

    public Long getApiNum() {
        return apiNum;
    }

    public void setApiNum(Long apiNum) {
        this.apiNum = apiNum;
    }

    public Long getAuthNum() {
        return authNum;
    }

    public void setAuthNum(Long authNum) {
        this.authNum = authNum;
    }

    public Long getAppNum() {
        return appNum;
    }

    public void setAppNum(Long appNum) {
        this.appNum = appNum;
    }
}
