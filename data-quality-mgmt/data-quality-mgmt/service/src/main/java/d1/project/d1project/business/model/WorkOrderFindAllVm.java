package d1.project.d1project.business.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

@ApiModel(value = "WorkOrderFindAllVm", description = "分页查找工单")
public class WorkOrderFindAllVm extends PageableVm {

    @ApiModelProperty("工单标题")
    private String title;
    @ApiModelProperty("处理人id")
    private String handlerId;
    @ApiModelProperty("级别：0正常、1一般、2关注、3警告、4严重警告")
    private String level;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }
}
