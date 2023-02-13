package d1.project.d1project.business.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "DataQualityReportStatisticsVm", description = "报告详情统计")
public class DataQualityReportStatisticsVm {

    @ApiModelProperty("开始时间：yyyy-MM")
    @NotBlank(message = "开始时间不能为空")
    private String startTime;

    @ApiModelProperty("结束时间：yyyy-MM")
    @NotBlank(message = "结束时间不能为空")
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
