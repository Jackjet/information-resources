package com.digitalchina.resourcecatalog.admin.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 资料中心文件关联表
 * </p>
 *
 * @author wangmh
 * @since 2020-05-21
 */
@ApiModel("资料中心文件关联表")
public class FileCenterRelDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    /**
     * 附件ID
     */
    @ApiModelProperty("附件Id")
    @NotNull(message = "附件Id不能为空！")
    private Integer storageId;

    /**
     * 文件说明
     */
    @ApiModelProperty("文件说明")
    private String fileRemark;

    /**
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    @NotNull(message = "文件名称不能为空！")
    private String fileName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStorageId() {
        return storageId;
    }

    public void setStorageId(Integer storageId) {
        this.storageId = storageId;
    }

    public String getFileRemark() {
        return fileRemark;
    }

    public void setFileRemark(String fileRemark) {
        this.fileRemark = fileRemark;
    }


    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
