package com.digitalchina.resourcecatalog.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author zhangys
 * @since 2020-05-14
 */
public class CataImportError implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 导入主表id
     */
    private Integer importId;

    /**
     * 错误信息
     */
    private String errorInfo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getImportId() {
        return importId;
    }

    public void setImportId(Integer importId) {
        this.importId = importId;
    }
    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

    public static final String ID = "id";

    public static final String IMPORT_ID = "import_id";

    public static final String ERROR_INFO = "error_info";

    @Override
    public String toString() {
        return "CataImportError{" +
        "id=" + id +
        ", importId=" + importId +
        ", errorInfo=" + errorInfo +
        "}";
    }
}
