package d1.project.resource.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 14:29
 */
@ApiModel(value = "WebAdminUserUpdateAllEnableVm", description = "系统管理_用户管理_启动禁用列表")
public class WebAdminUserUpdateAllEnableVm {

    @ApiModelProperty("ids")
    @Size(min = 1, message = "无效操作")
    private List<String> ids;

    @ApiModelProperty("true启用false禁用")
    @NotNull(message = "启用禁用不可为空")
    private boolean enable;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
