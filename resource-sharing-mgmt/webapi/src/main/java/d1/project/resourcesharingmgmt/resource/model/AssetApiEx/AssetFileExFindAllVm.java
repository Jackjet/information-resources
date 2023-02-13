package d1.project.resourcesharingmgmt.resource.model.AssetApiEx;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

/**
 * @author JungYoung
 */

@ApiModel(value = "AssetFileExFindAllVm", description = "云文件查询所有")
public class AssetFileExFindAllVm extends PageableVm {
    /**
     * 资源目录ID
     */
    @ApiModelProperty("资源目录ID,信息资源目录外键")
    private String uviewId;

    /**
     * 文件名称
     */
    @ApiModelProperty("api名称")
    private String name;

    /**
     * 文件格式
     */
    @ApiModelProperty("groupId名称")
    private String fileType;

    public String getUviewId() {
        return uviewId;
    }

    public void setUviewId(String uviewId) {
        this.uviewId = uviewId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
