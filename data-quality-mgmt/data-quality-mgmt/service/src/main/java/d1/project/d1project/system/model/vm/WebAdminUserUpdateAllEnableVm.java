package d1.project.d1project.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 批量启动禁用管理用户
 *
 * @author kikki
 * @date 2020-09-08 14:29
 */
@ApiModel(value = "WebAdminUserUpdateAllEnableVm", description = "批量启动禁用管理用户")
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
