package com.digitalchina.resourcecatalog.admin.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 文件上传关联表
 * </p>
 *
 * @author wangmh
 * @since 2020-05-21
 */
@ApiModel("上传文件")
public class FileUploadRelDto implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 资源目录id
     */
    @ApiModelProperty("资源目录id")
    @NotNull(message = "资源目录id不能为空！")
    private Integer subjectId;

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

    /**
     * 文件id
     */
    @ApiModelProperty("文件id")
    @NotNull(message = "文件id不能为空！")
    private Integer storageId;

    /**
     * 上传部门
     */
    @ApiModelProperty("部门id")
    @NotNull(message = "部门id不能为空！")
    private Integer deptId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
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

    public Integer getStorageId() {
        return storageId;
    }

    public void setStorageId(Integer storageId) {
        this.storageId = storageId;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }
}
