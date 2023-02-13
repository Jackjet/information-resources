package com.digitalchina.resourcecatalog.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 字典表
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
public class SysDict implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 编码名称
     */
    @NotNull(message = "编码名称不能为空")
    @ApiModelProperty("编码名称")
    private String name;

    /**
     * 编码代码
     */
    @NotNull(message = "编码代码不能为空")
    @ApiModelProperty("编码代码")
    private String value;

    /**
     * 编码类型
     */
    @NotNull(message = "编码类型不能为空")
    @ApiModelProperty("编码类型")
    private String type;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;

    /**
     * 排序（升序）
     */
    @ApiModelProperty("排序")
    private Integer sort;

    /**
     * 创建者
     */
    @ApiModelProperty("创建者ID")
    private Integer createBy;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime createDate;

    /**
     * 更新者
     */
    @ApiModelProperty("更新者ID")
    private Integer updateBy;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateDate;

    /**
     * 备注信息
     */
    @ApiModelProperty("备注信息")
    private String remarks;

    /**
     * 父节点ID
     */
    @ApiModelProperty("上级编码ID")
    private Integer pid;

    /**
     * 是否开发人员使用
     */
    @ApiModelProperty("是否开发人员使用，默认为1，开发人员使用。应用类的添加时为0")
    private Integer isInter;

    @TableField(exist = false)
    private List<SysDict> children;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
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
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }
    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getIsInter(){
        return isInter;
    }

    public void setIsInter(Integer isInter){
        this.isInter = isInter;
    }

    public static final String ID = "id";

    public static final String NAME = "name";

    public static final String VALUE = "value";

    public static final String TYPE = "type";

    public static final String DESCRIPTION = "description";

    public static final String SORT = "sort";

    public static final String CREATE_BY = "create_by";

    public static final String CREATE_DATE = "create_date";

    public static final String UPDATE_BY = "update_by";

    public static final String UPDATE_DATE = "update_date";

    public static final String REMARKS = "remarks";

    public static final String PID = "pid";

    public static final String IS_INTER = "is_inter";

    @Override
    public String toString() {
        return "SysDict{" +
        "id=" + id +
        ", name=" + name +
        ", value=" + value +
        ", type=" + type +
        ", description=" + description +
        ", sort=" + sort +
        ", createBy=" + createBy +
        ", createDate=" + createDate +
        ", updateBy=" + updateBy +
        ", updateDate=" + updateDate +
        ", remarks=" + remarks +
        ", pid=" + pid +
        ", isInter=" + isInter +
        "}";
    }

    public List<SysDict> getChildren() {
        return children;
    }

    public void setChildren(List<SysDict> children) {
        this.children = children;
    }
}
