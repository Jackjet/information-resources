package com.digitalchina.resourcecatalog.db.domain;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 信息需求目录报告
 * </p>
 *
 * @author baokd
 * @since 2020-05-25
 */
public class CataRequireReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("ID")
    private Integer id;

    /**
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    private String reportName;

    /**
     * 文件key
     */
    @ApiModelProperty("文件key")
    private String fileKey;

    /**
     * 下载时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("下载时间")
    private Date downloadTime;

    /**
     * 操作人
     */
    @ApiModelProperty("操作人")
    private String downloadBy;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 删除标志 0-未删除 1-已删除
     */
    @ApiModelProperty("删除标志 0-未删除 1-已删除")
    private Integer deleted;

    /**
     * 所属部门
     */
    @ApiModelProperty("所属部门")
    private Integer depId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }
    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }
    public Date getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(Date downloadTime) {
        this.downloadTime = downloadTime;
    }
    public String getDownloadBy() {
        return downloadBy;
    }

    public void setDownloadBy(String downloadBy) {
        this.downloadBy = downloadBy;
    }
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
    public Integer getDepId() {
        return depId;
    }

    public void setDepId(Integer depId) {
        this.depId = depId;
    }

    public static final String ID = "id";

    public static final String REPORT_NAME = "report_name";

    public static final String FILE_KEY = "file_key";

    public static final String DOWNLOAD_TIME = "download_time";

    public static final String DOWNLOAD_BY = "download_by";

    public static final String CREATE_BY = "create_by";

    public static final String DELETED = "deleted";

    public static final String DEP_ID = "dep_id";

    @Override
    public String toString() {
        return "CataRequireReport{" +
        "id=" + id +
        ", reportName=" + reportName +
        ", fileKey=" + fileKey +
        ", downloadTime=" + downloadTime +
        ", downloadBy=" + downloadBy +
        ", createBy=" + createBy +
        ", deleted=" + deleted +
        ", depId=" + depId +
        "}";
    }
}
