package com.digitalchina.resourcecatalog.admin.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author wangmh
 * @since 2020-04-01
 */
public class SysDictEditDto implements Serializable {

    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "id不能为空")
    private long id;
    /**
     * 标签名
     */
    @NotNull(message = "标签名不能为空")
    private String name;

    /**
     * 数据值
     */
    @NotNull(message = "数据值不能为空")
    private String value;

    /**
     * 类型
     */
    @NotNull(message = "类型不能为空")
    private String type;

    /**
     * 描述
     */
    @NotNull(message = "描述不能为空")
    private String description;

    /**
     * 排序（升序）
     */
    @NotNull(message = "排序方式不能为空")
    private BigDecimal sort;

    /**
     * 备注信息
     */
    private String remarks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getSort() {
        return sort;
    }

    public void setSort(BigDecimal sort) {
        this.sort = sort;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
