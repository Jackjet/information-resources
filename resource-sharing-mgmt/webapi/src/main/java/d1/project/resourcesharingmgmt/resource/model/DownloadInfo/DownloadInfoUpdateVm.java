package d1.project.resourcesharingmgmt.resource.model.DownloadInfo;

import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author zhengyang
 */
public class DownloadInfoUpdateVm {
    @ApiModelProperty("id")
    @NotBlank(message = "id不可为空")
    private String id;

    @ApiModelProperty("文件名称")
    @NotBlank(message = "文件名称不可为空")
    private String title;

    @ApiModelProperty("文件描述")
    private String fileDesc;

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("文件下载地址")
    @NotBlank(message = "文件下载地址不可为空")
    private String fileDownloadUri;

    @ApiModelProperty("文件类型")
    private String fileType;

    @ApiModelProperty("文件大小")
    private String fileSize;

    @ApiModelProperty("文件类别，1通知公告、2资料下载、3技术规范")
    private Integer type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileDesc() {
        return fileDesc;
    }

    public void setFileDesc(String fileDesc) {
        this.fileDesc = fileDesc;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
