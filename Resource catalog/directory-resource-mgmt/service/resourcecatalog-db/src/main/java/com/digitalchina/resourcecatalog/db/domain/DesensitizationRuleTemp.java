package com.digitalchina.resourcecatalog.db.domain;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 信息项（暂存表）脱敏规则表
 * </p>
 *
 * @author zhaoyan
 * @since 2020-06-18
 */
public class DesensitizationRuleTemp extends Model<DesensitizationRuleTemp> {

    private static final long serialVersionUID = 1L;

    /**
     *  主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty("主键")
    private Integer id;
    /**
     * 规则类型0-掩码规则，1-正则，2-字符替换
     */
    @NotNull(message = "规则类型不能为空")
    @ApiModelProperty("规则类型")
    private String type;
    /**
     * type为0时生效，后面保留位数
     */
    @ApiModelProperty("type为0时生效，后面保留位数")
    private Integer behind;
    /**
     * type为0时生效，前面保留位数
     */
    @ApiModelProperty("type为0时生效，前面保留位数")
    private Integer head;
    /**
     * 替换字符
     */
    @NotNull(message = "替换字符不能为空")
    @ApiModelProperty("替换字符")
    private String letter;
    /**
     * type为1时生效，正则规则
     */
    @ApiModelProperty("type为1时生效，正则规则")
    private String regular;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createBy;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private String updateBy;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;
    /**
     * 逻辑删除0-正常，1-禁用
     */
    @NotNull(message = "逻辑删除不能为空")
    @ApiModelProperty("逻辑删除")
    private Integer deleted;
    /**
     * 信息项id（与cata_info_item_temp_rel主键关联）
     */
    @NotNull(message = "信息项id不能为空")
    @ApiModelProperty("规则类型")
    private Integer itemId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getBehind() {
        return behind;
    }

    public void setBehind(Integer behind) {
        this.behind = behind;
    }

    public Integer getHead() {
        return head;
    }

    public void setHead(Integer head) {
        this.head = head;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
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

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "DesensitizationRuleTemp{" +
        ", id=" + id +
        ", type=" + type +
        ", behind=" + behind +
        ", head=" + head +
        ", letter=" + letter +
        ", regular=" + regular +
        ", createBy=" + createBy +
        ", createTime=" + createTime +
        ", updateBy=" + updateBy +
        ", updateTime=" + updateTime +
        ", deleted=" + deleted +
        ", itemId=" + itemId +
        "}";
    }
}
