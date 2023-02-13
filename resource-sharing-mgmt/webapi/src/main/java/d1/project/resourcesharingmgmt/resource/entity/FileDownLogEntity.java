package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.*;
import java.util.Calendar;

/**
 * 资源目录挂接文件下载记录
 *
 * @author zhengyang
 */
@Entity
@Table(name = "d1_file_down_log")
@ApiModel(value = "FileDownLogEntity", description = "资源目录挂接文件下载记录")
public class FileDownLogEntity extends BaseCreateEntity {
    @ApiModelProperty("资源使用ID")
    @Column(length = 32)
    private String resourceUseInfoId;

    /**
     * 文件下载地址
     */
    @ApiModelProperty("文件下载地址")
    @Column(columnDefinition = "TEXT")
    private String fileDownloadUri;

    @ApiModelProperty("下载时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar downloadTime;

    public String getResourceUseInfoId() {
        return resourceUseInfoId;
    }

    public void setResourceUseInfoId(String resourceUseInfoId) {
        this.resourceUseInfoId = resourceUseInfoId;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public Calendar getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(Calendar downloadTime) {
        this.downloadTime = downloadTime;
    }
}
