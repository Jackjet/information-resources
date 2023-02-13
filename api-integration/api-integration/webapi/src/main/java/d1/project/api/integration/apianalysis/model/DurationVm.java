package d1.project.api.integration.apianalysis.model;

import java.util.List;

/**
 * @author baozh
 */
public class DurationVm {
    private AvgDurationVm avg;
    private List<NameNumVm> durationNum;

    public AvgDurationVm getAvg() {
        return avg;
    }

    public void setAvg(AvgDurationVm avg) {
        this.avg = avg;
    }

    public List<NameNumVm> getDurationNum() {
        return durationNum;
    }

    public void setDurationNum(List<NameNumVm> durationNum) {
        this.durationNum = durationNum;
    }
}
