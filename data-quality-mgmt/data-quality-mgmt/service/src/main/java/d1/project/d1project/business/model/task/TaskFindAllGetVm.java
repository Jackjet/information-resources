package d1.project.d1project.business.model.task;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

@ApiModel(value = "TaskFindAllGetVm", description = "查询任务组")
public class TaskFindAllGetVm extends PageableVm {
    @ApiModelProperty("任务名称")
    private String name;
    @ApiModelProperty("任务状态 0 未启动，1 已启动")
    private String status ;
    @ApiModelProperty("组id")
    private String groupId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
