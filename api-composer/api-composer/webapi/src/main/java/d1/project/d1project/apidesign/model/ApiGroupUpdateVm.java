package d1.project.d1project.apidesign.model;

import javax.validation.constraints.NotBlank;

/**
 * @author baozh
 */
public class ApiGroupUpdateVm {
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
