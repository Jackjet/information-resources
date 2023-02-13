package com.digitalchina.resourcecatalog.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhangys
 * @since 2020-05-12
 */
public class CataImport implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("ID")
    private Integer id;

    /**
     * 文件KEY
     */
    @ApiModelProperty("文件KEY")
    private String fileKey;


    /**
     * 导入状态（1、成功；2、失败）
     */
    @ApiModelProperty("导入状态（1、成功；2、失败）")
    private String importStatus;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 删除标志
     */
    @ApiModelProperty("删除标志(1、已删除；0、正常)")
    private Integer deleted;

    /**
     * 是否有提示信息
     */
    @ApiModelProperty("是否有提示信息(1、有提示；0、无)")
    private String hasInfo;

    /**
     * 所属部门
     */
    @ApiModelProperty("所属部门")
    private Integer depId;

    /**
     * 文件名称
     */
    @TableField(exist = false)
    @ApiModelProperty("文件名称")
    private String fileName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getFileKey() {
        return fileKey;
    }

    public void setFileKey(String fileId) {
        this.fileKey = fileId;
    }
    public String getImportStatus() {
        return importStatus;
    }

    public void setImportStatus(String importStatus) {
        this.importStatus = importStatus;
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
    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getHasInfo() {
        return hasInfo;
    }

    public void setHasInfo(String hasInfo) {
        this.hasInfo = hasInfo;
    }

    public Integer getDepId() {
        return depId;
    }

    public void setDepId(Integer depId) {
        this.depId = depId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public static final String ID = "id";

    public static final String FILE_KEY = "file_key";

    public static final String IMPORT_STATUS = "import_status";

    public static final String EXCEPTION_INFO = "exception_info";

    public static final String CREATE_BY = "create_by";

    public static final String CREATE_TIME = "create_time";

    public static final String DELETED = "deleted";

    public static final String HAS_INFO = "has_info";

    public static final String DEP_ID = "dep_id";

    @Override
    public String toString() {
        return "CataImport{" +
        "id=" + id +
        ", fileKey=" + fileKey +
        ", importStatus=" + importStatus +
        ", createBy=" + createBy +
        ", createTime=" + createTime +
        ", deleted=" + deleted +
                ", hasInfo=" + hasInfo +
        "}";
    }
}
