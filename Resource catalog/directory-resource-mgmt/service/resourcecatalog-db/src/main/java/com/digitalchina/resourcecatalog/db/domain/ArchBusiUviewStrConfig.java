package com.digitalchina.resourcecatalog.db.domain;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 信息项属性表
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public class ArchBusiUviewStrConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 信息项名称
     */
    @NotNull(message = "信息项名称不能为空")
    @ApiModelProperty("信息项名称")
    private String itemName;

    /**
     * 信息项编码
     */
    @NotNull(message = "信息项编码不能为空")
    @ApiModelProperty("信息项编码")
    private String itemMark;

    /**
     * 数据编码名称
     */
    @ApiModelProperty("数据编码名称")
    private String itemCode;

    /**
     * 是否必选 0-非必选 1-必选
     */
    @NotNull(message = "是否必选不能为空")
    @ApiModelProperty("是否必选 0-非必选 1-必选")
    private String isChoice;

    /**
     * 是否内置 0-否 1-是
     */
    @ApiModelProperty("是否内置 0-否 1-是")
    private String isInside;

    /**
     * 组件类型 与dict关联（文本框、选择框......）
     */
    @NotNull(message = "组件类型必选")
    @ApiModelProperty("组件类型")
    private String itemType;

    /**
     * 信息项排序
     */
    @NotNull(message = "信息项排序不能为空")
    @ApiModelProperty("信息项排序")
    private Integer orderNum;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public String getItemMark() {
        return itemMark;
    }

    public void setItemMark(String itemMark) {
        this.itemMark = itemMark;
    }
    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    public String getIsChoice() {
        return isChoice;
    }

    public void setIsChoice(String isChoice) {
        this.isChoice = isChoice;
    }
    public String getIsInside() {
        return isInside;
    }

    public void setIsInside(String isInside) {
        this.isInside = isInside;
    }
    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
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

    public static final String ID = "id";

    public static final String ITEM_NAME = "item_name";

    public static final String ITEM_MARK = "item_mark";

    public static final String ITEM_CODE = "item_code";

    public static final String IS_CHOICE = "is_choice";

    public static final String IS_INSIDE = "is_inside";

    public static final String ITEM_TYPE = "item_type";

    public static final String ORDER_NUM = "order_num";

    public static final String CREATE_BY = "create_by";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_BY = "update_by";

    public static final String UPDATE_TIME = "update_time";

    @Override
    public String toString() {
        return "ArchBusiUviewStrConfig{" +
        "id=" + id +
        ", itemName=" + itemName +
        ", itemMark=" + itemMark +
        ", itemCode=" + itemCode +
        ", isChoice=" + isChoice +
        ", isInside=" + isInside +
        ", itemType=" + itemType +
        ", orderNum=" + orderNum +
        ", createBy=" + createBy +
        ", createTime=" + createTime +
        ", updateBy=" + updateBy +
        ", updateTime=" + updateTime +
        "}";
    }
}
