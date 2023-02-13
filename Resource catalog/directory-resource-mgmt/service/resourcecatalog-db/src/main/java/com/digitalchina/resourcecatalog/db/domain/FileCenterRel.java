package com.digitalchina.resourcecatalog.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 资料中心文件关联表
 * </p>
 *
 * @author wangmh
 * @since 2020-05-21
 */
public class FileCenterRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 附件ID
     */
    private Integer storageId;

    /**
     * 文件说明
     */
    private String fileRemark;

    /**
     * 发布人
     */
    private String createBy;

    /**
     * 发布时间
     */
    private Date createTime;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 下载次数
     */
    private Integer dwCount;

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

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getDwCount() {
        return dwCount;
    }

    public void setDwCount(Integer dwCount) {
        this.dwCount = dwCount;
    }

    public static final String ID = "id";

    public static final String STORAGE_ID = "storage_id";

    public static final String FILE_REMARK = "file_remark";

    public static final String CREATE_BY = "create_by";

    public static final String CREATE_TIME = "create_time";

    public static final String FILE_NAME = "file_name";

    public static final String DW_COUNT = "dw_count";

    @Override
    public String toString() {
        return "FileCenterRel{" +
                "id=" + id +
                ", storageId=" + storageId +
                ", fileRemark=" + fileRemark +
                ", createBy=" + createBy +
                ", createTime=" + createTime +
                ", fileName=" + fileName +
                ", dwCount=" + dwCount +
                "}";
    }
}
