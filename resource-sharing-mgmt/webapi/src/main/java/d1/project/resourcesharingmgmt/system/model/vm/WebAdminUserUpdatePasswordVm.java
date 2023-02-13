package d1.project.resourcesharingmgmt.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-11 10:06
 */
@ApiModel(value = "修改密码")
public class WebAdminUserUpdatePasswordVm {

    @NotBlank(message = "id不能为空")
    @ApiModelProperty("id")
    private final String id;

    @NotBlank(message = "老密码不能为空")
    @ApiModelProperty("老密码")
    private final String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @ApiModelProperty("新密码")
    private final String newPassword;

    public WebAdminUserUpdatePasswordVm(String id, String oldPassword, String newPassword) {
        this.id = id;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getId() {
        return id;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

}
