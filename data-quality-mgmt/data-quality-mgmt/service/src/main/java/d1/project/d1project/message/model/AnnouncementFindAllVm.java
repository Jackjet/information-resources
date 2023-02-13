package d1.project.d1project.message.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.project.d1project.common.model.TimePageableVm;

/**
 * 分页查询通知公告
 *
 * @author kikki
 * @date 2020-11-19 16:09
 */
@ApiModel(value = "AnnouncementFindAllVm", description = "分页查询通知公告")
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
