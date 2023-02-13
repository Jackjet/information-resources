package d1.project.d1project.message.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * 更新通知公告
 *
 * @author kikki
 * @date 2020-11-19 16:09
 */
@ApiModel(value = "AnnouncementUpdateVm", description = "更新通知公告")
public class AnnouncementUpdateVm extends AnnouncementBase {

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
