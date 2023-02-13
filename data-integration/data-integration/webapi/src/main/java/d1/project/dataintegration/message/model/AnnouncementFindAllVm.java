package d1.project.dataintegration.message.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.project.dataintegration.common.model.TimePageableVm;

/**
 * MY_JAR_NAME
 *
 * @author kikki
 * @date 2020-11-19 16:09
 */
@ApiModel(value = "AnnouncementFindAllVm", description = "通知公告分页查询")
public class AnnouncementFindAllVm extends TimePageableVm {

    @ApiModelProperty("标题")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
