package d1.project.d1project.business.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

@ApiModel(value = "DataQualityReportFindAllVm", description = "分页查找数据质量报告")
public class DataQualityReportFindAllVm extends PageableVm {

    @ApiModelProperty("报告名称")
    private String name;

    @ApiModelProperty("创建人名称")
    private String createByName;

    @ApiModelProperty("创建人名称")
    private String createTime;

    @ApiModelProperty("开始时间：yyyy-MM")
    private String startTime;

    @ApiModelProperty("结束时间：yyyy-MM")
    private String endTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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
