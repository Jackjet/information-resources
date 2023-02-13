package d1.project.tangshan.operation.manage.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 用于查询时间范围
 *
 * @author Kikki
 */
@ApiModel(value = "TimePageableVm", description = "时间段分页查询信息")
public class TimePageableVm extends PageableVm {

    @ApiModelProperty(value = "时间开始")
    private String startTime;

    @ApiModelProperty(value = "时间结束")
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
