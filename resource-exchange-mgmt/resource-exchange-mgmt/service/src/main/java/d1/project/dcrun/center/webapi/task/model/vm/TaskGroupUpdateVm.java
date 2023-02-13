package d1.project.dcrun.center.webapi.task.model.vm;

import javax.validation.constraints.NotBlank;

/**
 * @author baozh
 */
public class TaskGroupUpdateVm {
    @NotBlank(message = "请输入名称")
    private String name;
    private String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
