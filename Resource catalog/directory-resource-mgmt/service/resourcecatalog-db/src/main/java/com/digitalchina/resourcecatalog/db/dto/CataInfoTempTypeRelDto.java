package com.digitalchina.resourcecatalog.db.dto;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 信息资源目录与信息类型关联表(暂存表)
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public class CataInfoTempTypeRelDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 信息资源ID
     */
    @ApiModelProperty("信息资源ID")
    private Integer infoId;

    /**
     * 类型 1-基础信息 2-主题信息 3-部门信息
     */
    @ApiModelProperty("类型 1-基础信息 2-主题信息 3-部门信息")
    private String type;

    /**
     * 所属类型ID,关联dict_asset_cate表ID
     */
    @ApiModelProperty("所属类型ID")
    private Integer typeId;
    
    /**
     * 所属类型名称
     */
    @ApiModelProperty("所属类型名称")
    private String typeName;

    public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

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
        return "CataInfoTempTypeRel{" +
        "infoId=" + infoId +
        ", type=" + type +
        ", typeId=" + typeId +
        "}";
    }
}
