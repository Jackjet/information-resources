package d1.project.d1project.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

/**
 * 分页查找角色
 *
 * @author kikki
 * @date 2020-09-08 14:29
 */
@ApiModel(value = "RoleFindAllVm", description = "分页查找角色")
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
