package d1.project.api.integration.apianalysis.model;

import java.util.Calendar;

/**
 * @author baozh
 */
public class TimeVm {
    private Calendar startTime;
    private Calendar endTime;

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }
}
