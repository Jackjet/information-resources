package d1.project.api.integration.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 14:29
 */
@ApiModel(value = "WebAdminUserUpdateAllEnableVm", description = "系统管理_用户管理_启动禁用")
public class WebAdminUserUpdateEnableVm {

    @ApiModelProperty("id")
    @NotBlank(message = "id不可为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
