package d1.project.d1project.business.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "DataQualityReportInsertVm", description = "添加数据质量报告")
public class DataQualityReportInsertVm {

    @ApiModelProperty("报告名称")
    @NotBlank(message = "报告名称不可为空")
    private String name;

    @ApiModelProperty("开始时间：yyyy-MM")
    private String startTime;

    @ApiModelProperty("结束时间：yyyy-MM")
    private String endTime;

    @ApiModelProperty("规则模板描述")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
