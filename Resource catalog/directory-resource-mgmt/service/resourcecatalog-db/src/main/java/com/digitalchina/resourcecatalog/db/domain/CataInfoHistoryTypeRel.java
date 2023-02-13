package com.digitalchina.resourcecatalog.db.domain;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 信息资源目录历史记录与信息类型关联表
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public class CataInfoHistoryTypeRel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 信息资源历史信息ID
     */
    @NotNull(message = "信息资源历史信息ID不能为空")
    @ApiModelProperty("信息资源历史信息ID")
    private Integer infoId;

    /**
     * 类型 1-基础信息 2-主题信息 3-部门信息
     */
    @NotNull(message = "类型不能为空")
    @ApiModelProperty("类型 1-基础信息 2-主题信息 3-部门信息")
    private String type;

    /**
     * 所属类型ID,关联dict_asset_cate表ID
     */
    @NotNull(message = "所属类型ID不能为空")
    @ApiModelProperty("所属类型ID")
    private Integer typeId;

    public Integer getInfoId() {
        return infoId;
    }

    public void setInfoId(Integer infoId) {
        this.infoId = infoId;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public static final String INFO_ID = "info_id";

    public static final String TYPE = "type";

    public static final String TYPE_ID = "type_id";

    @Override
    public String toString() {
        return "CataInfoHistoryTypeRel{" +
        "infoId=" + infoId +
        ", type=" + type +
        ", typeId=" + typeId +
        "}";
    }
}
