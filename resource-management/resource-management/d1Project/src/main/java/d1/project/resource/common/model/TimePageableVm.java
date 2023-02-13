package d1.project.resource.common.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 20:32
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