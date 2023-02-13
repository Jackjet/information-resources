package d1.project.d1project.business.model.task;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "TaskEnableOrForbiddenPutVm", description = "启动或停止任务")
public class TaskEnableOrForbiddenPutVm {
    @NotBlank(message = "任务id不能为空")
    @ApiModelProperty("任务id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
