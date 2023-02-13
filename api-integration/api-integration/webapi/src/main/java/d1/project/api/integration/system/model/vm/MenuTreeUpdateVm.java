package d1.project.api.integration.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.project.api.integration.system.model.MenuTreeBase;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-10 14:43
 */
@ApiModel(value = "MenuTreeUpdateVm", description = "菜单修改")
public class MenuTreeUpdateVm extends MenuTreeBase {

    @ApiModelProperty("id")
    @NotBlank(message = "id不可为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @ApiModelProperty("是否是系统初始化 0 否、1 是")
    private String hasSystem;

    public String getHasSystem() {
        return hasSystem;
    }

    public void setHasSystem(String hasSystem) {
        this.hasSystem = hasSystem;
    }
}
