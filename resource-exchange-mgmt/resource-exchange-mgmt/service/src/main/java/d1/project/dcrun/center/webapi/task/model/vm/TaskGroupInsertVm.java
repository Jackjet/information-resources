package d1.project.dcrun.center.webapi.task.model.vm;

import javax.validation.constraints.NotEmpty;

/**
 * @author baozh
 */
public class TaskGroupInsertVm {
    @NotEmpty(message = "请输入名称")
    private String name;

    private String parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
