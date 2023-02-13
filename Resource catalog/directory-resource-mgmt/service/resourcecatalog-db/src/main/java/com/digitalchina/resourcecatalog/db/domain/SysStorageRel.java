package com.digitalchina.resourcecatalog.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

/**
 * <p>
 * 附件关联表
 * </p>
 *
 * @author wangmh
 * @since 2020-04-30
 */
public class SysStorageRel implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 附件ID
     */
    private Integer storageId;

    /**
     * 主体ID，例如报修单ID
     */
    private Integer subjectId;

    /**
     * 附件类型 1:报修附件 2:物业维修附件 3:地下室 4:房屋 5-车位 6-车牌 7-底商
     */
    private String subjectType;

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
    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public static final String ID = "id";

    public static final String STORAGE_ID = "storage_id";

    public static final String SUBJECT_ID = "subject_id";

    public static final String SUBJECT_TYPE = "subject_type";

    @Override
    public String toString() {
        return "SysStorageRel{" +
        "id=" + id +
        ", storageId=" + storageId +
        ", subjectId=" + subjectId +
        ", subjectType=" + subjectType +
        "}";
    }
}
