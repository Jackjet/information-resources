package d1.project.d1project.business.model.task.log;

import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

import javax.validation.constraints.NotBlank;

public class TaskLogFindAllGetVm extends PageableVm {
    @NotBlank(message = "任务id不能为空")
    @ApiModelProperty("任务Id")
    private String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
