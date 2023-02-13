package d1.project.api.integration.message.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * MY_JAR_NAME
 *
 * @author kikki
 * @date 2020-11-19 16:09
 */
@ApiModel(value = "AnnouncementInsertVm", description = "通知公告修改")
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
