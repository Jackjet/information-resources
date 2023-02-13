package d1.project.resourcesharingmgmt.resource.model.DataPush;

import d1.framework.webapi.annotation.ApiModel;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "DataPushLogInsertVm", description = "推送数据详情")
public class DataPushLogInsertVm {
    /**
     * 提供单位
     */
    @NotBlank(message = "提供单位不能为空")
    private String from;

    /**
     * 申请单位
     */
    @NotBlank(message = "申请单位不能为空")
    private String to;

    /**
     * 推送数量
     */
    private Integer num;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
}
