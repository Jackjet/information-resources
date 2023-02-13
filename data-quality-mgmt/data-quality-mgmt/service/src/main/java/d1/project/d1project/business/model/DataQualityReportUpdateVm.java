package d1.project.d1project.business.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "DataQualityReportUpdateVm", description = "完善数据质量报告")
public class DataQualityReportUpdateVm {

    @ApiModelProperty("id")
    @NotBlank(message = "报告id不可为空")
    private String id;

    @ApiModelProperty("前言")
    @NotBlank(message = "报告前言不可为空")
    private String foreword;

    @ApiModelProperty("结束语")
    @NotBlank(message = "报告结束语不可为空")
    private String closing;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
