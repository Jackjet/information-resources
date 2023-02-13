package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 下载中心
 *
 * @author zhengyang
 */
@Entity
@Table(name = "d1_download_info")
@ApiModel(value = "DownloadInfoEntity", description = "下载中心")
public class DownloadInfoEntity extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("文件名称")
    @Column(length = 200)
    private String title;

    @ApiModelProperty("文件描述")
    @Column(columnDefinition = "TEXT")
    private String fileDesc;

    @ApiModelProperty("下载量")
    private int downCount;

    @ApiModelProperty("文件名")
    @Column(length = 500)
    private String fileName;

    @ApiModelProperty("文件下载地址")
    @Column(columnDefinition = "TEXT")
    private String fileDownloadUri;

    @ApiModelProperty("文件类型")
    @Column(length = 215)
    private String fileType;

    @ApiModelProperty("文件大小")
    @Column(length = 50)
    private String fileSize;

    @ApiModelProperty("文件类别，1通知公告、2资料下载、3技术规范")
    private Integer type;

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

    public int getDownCount() {
        return downCount;
    }

    public void setDownCount(int downCount) {
        this.downCount = downCount;
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
