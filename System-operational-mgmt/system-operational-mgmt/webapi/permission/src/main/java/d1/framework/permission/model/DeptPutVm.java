package d1.framework.permission.model;

import io.swagger.annotations.ApiModelProperty;

public class DeptPutVm {

    private String id;
    @ApiModelProperty(value = "名称")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
