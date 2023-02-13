package com.digitalchina.resourcecatalog.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 文件上传关联表
 * </p>
 *
 * @author wangmh
 * @since 2020-05-21
 */
public class FileUploadRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 附件ID
     */
    private Integer storageId;

    /**
     * 资源目录id
     */
    private Integer subjectId;

    /**
     * 审核状态 0等待审核 1通过 2驳回
     */
    private String status;

    /**
     * 文件说明
     */
    private String fileRemark;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 审核人
     */
    private String updateBy;

    /**
     * 审核时间
     */
    private Date updateTime;

    /**
     * 审核意见
     */
    private String comment;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 上传部门
     */
    private Integer deptId;

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

    public Integer getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Integer subjectId) {
        this.subjectId = subjectId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public static final String ID = "id";

    public static final String STORAGE_ID = "storage_id";

    public static final String SUBJECT_ID = "subject_id";

    public static final String STATUS = "status";

    public static final String FILE_REMARK = "file_remark";

    public static final String FILE_NAME = "file_name";

    public static final String UPDATE_BY = "update_by";

    public static final String UPDATE_TIME = "update_time";

    public static final String COMMENT = "comment";

    public static final String USER_ID = "user_id";

    public static final String DEPT_ID = "dept_id";

    @Override
    public String toString() {
        return "FileUploadRel{" +
                "id=" + id +
                ", storageId=" + storageId +
                ", subjectId=" + subjectId +
                ", status=" + status +
                ", fileRemark=" + fileRemark +
                ", fileName=" + fileName +
                ", updateBy=" + updateBy +
                ", updateTime=" + updateTime +
                ", comment=" + comment +
                ", userId=" + userId +
                ", deptId=" + deptId +
                "}";
    }
}
