package d1.project.resourcesharingmgmt.resource.model.DownloadInfo;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

/**
 * @author JungYoung
 */

@ApiModel(value = "DownloadInfoFindAllVm", description = "下载中心查询所有")
public class DownloadInfoFindAllVm extends PageableVm {
    @ApiModelProperty("文件名称")
    private String title;

    @ApiModelProperty("文件类别，1通知公告、2资料下载、3技术规范")
    private Integer type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
