package d1.project.api.integration.apianalysis.model;

/**
 * @author baozh
 */
public class AvgDurationVm {
    private Double avgNum;
    private Long slowest;
    private Long fastest;

    public Double getAvgNum() {
        return avgNum;
    }

    public void setAvgNum(Double avgNum) {
        this.avgNum = avgNum;
    }

    public Long getSlowest() {
        return slowest;
    }

    public void setSlowest(Long slowest) {
        this.slowest = slowest;
    }

    public Long getFastest() {
        return fastest;
    }

    public void setFastest(Long fastest) {
        this.fastest = fastest;
    }
}
