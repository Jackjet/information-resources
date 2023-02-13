package d1.project.d1project.business.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "d1_data_quality_report")
@ApiModel(value = "DataQualityReport", description = "数据质量报告")
public class DataQualityReport extends BaseCreateAndUpdateEntity {

    @ApiModelProperty("模板名称")
    private String name;

    @ApiModelProperty("开始时间：yyyy-MM")
    private String startTime;

    @ApiModelProperty("结束时间：yyyy-MM")
    private String endTime;

    @ApiModelProperty("模板描述")
    @Column(columnDefinition = "TEXT")
    private String description;

    @ApiModelProperty("创建人名称")
    private String createByName;

    @ApiModelProperty("前言")
    @Column(columnDefinition = "TEXT")
    private String foreword;

    @ApiModelProperty("结束语")
    @Column(columnDefinition = "TEXT")
    private String closing;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getForeword() {
        return foreword;
    }

    public void setForeword(String foreword) {
        this.foreword = foreword;
    }

    public String getClosing() {
        return closing;
    }

    public void setClosing(String closing) {
        this.closing = closing;
    }
}
