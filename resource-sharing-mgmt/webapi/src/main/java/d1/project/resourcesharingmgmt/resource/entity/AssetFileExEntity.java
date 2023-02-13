package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 信息资源目录与云文件关联表
 *
 * @author zhengyang
 */
@Entity
@Table(name = "d1_asset_file_ex")
@ApiModel(value = "AssetFileExEntity", description = "信息资源目录与文件关联表")
public class AssetFileExEntity extends BaseCreateEntity {
    @ApiModelProperty("信息资源ID")
    @Column(length = 32)
    private String uviewId;

    @ApiModelProperty("信息资源名称")
    @Column(length = 225)
    private String uviewNm;

    @ApiModelProperty("文件名称")
    @Column(length = 100)
    private String name;

    @ApiModelProperty("文件下载地址")
    @Column(columnDefinition = "TEXT")
    private String fileDownloadUri;

    @ApiModelProperty("文件类型")
    private String fileType;

    @ApiModelProperty("描述")
    @Column(columnDefinition = "TEXT")
    private String detail;

    public String getUviewId() {
        return uviewId;
    }

    public void setUviewId(String uviewId) {
        this.uviewId = uviewId;
    }

    public String getUviewNm() {
        return uviewNm;
    }

    public void setUviewNm(String uviewNm) {
        this.uviewNm = uviewNm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
