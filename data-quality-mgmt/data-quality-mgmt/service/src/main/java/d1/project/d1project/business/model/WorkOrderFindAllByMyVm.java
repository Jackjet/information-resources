package d1.project.d1project.business.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

@ApiModel(value = "WorkOrderFindAllByMyVm", description = "分页查找与我相关的工单")
public class WorkOrderFindAllByMyVm extends PageableVm {
    @ApiModelProperty("级别：-1我的工单、0我的代办、1我已处理")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
