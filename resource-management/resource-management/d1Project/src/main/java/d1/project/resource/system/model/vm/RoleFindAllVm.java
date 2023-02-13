package d1.project.resource.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 14:29
 */
@ApiModel(value = "RoleFindAllVm", description = "系统管理_角色_查找全部")
public class RoleFindAllVm extends PageableVm {

    @ApiModelProperty("姓名")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
