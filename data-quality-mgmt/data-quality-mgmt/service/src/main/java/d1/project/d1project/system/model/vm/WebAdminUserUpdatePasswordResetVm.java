package d1.project.d1project.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * 批量重置管理用户密码
 *
 * @author kikki
 * @date 2020-09-15 20:07
 */
@ApiModel(value = "WebAdminUserUpdatePasswordResetVm", description = "批量重置管理用户密码")
public class WebAdminUserUpdatePasswordResetVm {
    @ApiModelProperty("ids列表")
    @Size(min = 1, message = "无效操作")
    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
