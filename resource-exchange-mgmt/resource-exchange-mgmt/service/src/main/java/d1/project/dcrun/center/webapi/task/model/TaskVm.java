package d1.project.dcrun.center.webapi.task.model;


import d1.project.dcrun.center.webapi.task.entity.Task;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author zhengyang
 */
public class TaskVm extends Task {
    @ApiModelProperty("容器名称")
    private String containerName;

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }
}
