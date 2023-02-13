package d1.project.d1project.business.model.task.log;

import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotNull;

public class TaskRuleLogFindAllGetVm {
    @NotNull(message = "任务日志id不能为空")
    @ApiModelProperty("任务日志id")
    private String taskLogId;

    public String getTaskLogId() {
        return taskLogId;
    }

    public void setTaskLogId(String taskLogId) {
        this.taskLogId = taskLogId;
    }
}
