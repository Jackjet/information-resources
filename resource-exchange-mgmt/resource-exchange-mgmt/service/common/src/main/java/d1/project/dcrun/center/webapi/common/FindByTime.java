package d1.project.dcrun.center.webapi.common;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author chengh
 */
public class FindByTime extends PageableVm {
    @ApiModelProperty(value = "开始时间")
    private String startTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
