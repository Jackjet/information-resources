package d1.framework.permission.model;

import d1.framework.webapi.model.PageableVm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(value = "RoleGetVm", description = "查询角色列表条件model")
public class RoleGetVm extends PageableVm {
    @ApiModelProperty(value = "角色名称")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
