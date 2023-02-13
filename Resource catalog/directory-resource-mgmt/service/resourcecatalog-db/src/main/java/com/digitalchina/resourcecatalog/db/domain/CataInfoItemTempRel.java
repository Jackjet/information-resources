package com.digitalchina.resourcecatalog.db.domain;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 信息项(暂存表)
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public class CataInfoItemTempRel implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("id")
    @TableId(value = "uviewstr_id", type = IdType.AUTO)
    private Integer uviewstrId;

    /**
     * 资源目录ID
     */
    @NotNull(message = "资源目录ID不能为空")
    @ApiModelProperty("资源目录ID")
    private Integer uviewId;

    /**
     * 信息项名称
     */
    @ApiModelProperty("信息项名称")
    private String srcField;

    /**
     * 信息项英文标识
     */
    @ApiModelProperty("信息项英文标识")
    private String engCd;

    /**
     * 信息项数据类型
     */
    @ApiModelProperty("信息项数据类型")
    private String srcDataTyp;

    /**
     * 信息项数据长度
     */
    @ApiModelProperty("信息项数据长度")
    private String dataLen;

    /**
     * 信息项描述
     */
    @ApiModelProperty("信息项描述")
    private String itemRemark;

    /**
     * 显示序号
     */
    @ApiModelProperty("显示序号")
    private Integer sno;

    /**
     * 扩展1
     */
    @ApiModelProperty("扩展1")
    private String ext1;

    /**
     * 扩展2
     */
    @ApiModelProperty("扩展2")
    private String ext2;

    /**
     * 扩展3
     */
    @ApiModelProperty("扩展3")
    private String ext3;

    /**
     * 扩展4
     */
    @ApiModelProperty("扩展4")
    private String ext4;

    /**
     * 扩展5
     */
    @ApiModelProperty("扩展5")
    private String ext5;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;
    @ApiModelProperty("扩展6")
    private String ext6;
    
    @ApiModelProperty("扩展7")
    private String ext7;
    
    @ApiModelProperty("扩展8")
    private String ext8;
    
    @ApiModelProperty("扩展9")
    private String ext9;
    
    @ApiModelProperty("扩展10")
    private String ext10;
    
    @ApiModelProperty("扩展11")
    private String ext11;
    
    @ApiModelProperty("扩展12")
    private String ext12;
    
    @ApiModelProperty("扩展13")
    private String ext13;
    
    @ApiModelProperty("扩展14")
    private String ext14;
    
    @ApiModelProperty("扩展15")
    private String ext15;
    
    @ApiModelProperty("扩展16")
    private String ext16;
    
    @ApiModelProperty("扩展17")
    private String ext17;
    
    @ApiModelProperty("扩展18")
    private String ext18;
    
    @ApiModelProperty("扩展19")
    private String ext19;
    
    @ApiModelProperty("扩展20")
    private String ext20;
    
    public String getExt6() {
		return ext6;
	}

	public void setExt6(String ext6) {
		this.ext6 = ext6;
	}

	public String getExt7() {
		return ext7;
	}

	public void setExt7(String ext7) {
		this.ext7 = ext7;
	}

	public String getExt8() {
		return ext8;
	}

	public void setExt8(String ext8) {
		this.ext8 = ext8;
	}

	public String getExt9() {
		return ext9;
	}

	public void setExt9(String ext9) {
		this.ext9 = ext9;
	}

	public String getExt10() {
		return ext10;
	}

	public void setExt10(String ext10) {
		this.ext10 = ext10;
	}

	public String getExt11() {
		return ext11;
	}

	public void setExt11(String ext11) {
		this.ext11 = ext11;
	}

	public String getExt12() {
		return ext12;
	}

	public void setExt12(String ext12) {
		this.ext12 = ext12;
	}

	public String getExt13() {
		return ext13;
	}

	public void setExt13(String ext13) {
		this.ext13 = ext13;
	}

	public String getExt14() {
		return ext14;
	}

	public void setExt14(String ext14) {
		this.ext14 = ext14;
	}

	public String getExt15() {
		return ext15;
	}

	public void setExt15(String ext15) {
		this.ext15 = ext15;
	}

	public String getExt16() {
		return ext16;
	}

	public void setExt16(String ext16) {
		this.ext16 = ext16;
	}

	public String getExt17() {
		return ext17;
	}

	public void setExt17(String ext17) {
		this.ext17 = ext17;
	}

	public String getExt18() {
		return ext18;
	}

	public void setExt18(String ext18) {
		this.ext18 = ext18;
	}

	public String getExt19() {
		return ext19;
	}

	public void setExt19(String ext19) {
		this.ext19 = ext19;
	}

	public String getExt20() {
		return ext20;
	}

	public void setExt20(String ext20) {
		this.ext20 = ext20;
	}

	public Integer getUviewstrId() {
        return uviewstrId;
    }

    public void setUviewstrId(Integer uviewstrId) {
        this.uviewstrId = uviewstrId;
    }
    public Integer getUviewId() {
        return uviewId;
    }

    public void setUviewId(Integer uviewId) {
        this.uviewId = uviewId;
    }
    public String getSrcField() {
        return srcField;
    }

    public void setSrcField(String srcField) {
        this.srcField = srcField;
    }
    public String getEngCd() {
        return engCd;
    }

    public void setEngCd(String engCd) {
        this.engCd = engCd;
    }
    public String getSrcDataTyp() {
        return srcDataTyp;
    }

    public void setSrcDataTyp(String srcDataTyp) {
        this.srcDataTyp = srcDataTyp;
    }
    public String getDataLen() {
        return dataLen;
    }

    public void setDataLen(String dataLen) {
        this.dataLen = dataLen;
    }
    public String getItemRemark() {
        return itemRemark;
    }

    public void setItemRemark(String itemRemark) {
        this.itemRemark = itemRemark;
    }
    public Integer getSno() {
        return sno;
    }

    public void setSno(Integer sno) {
        this.sno = sno;
    }
    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }
    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }
    public String getExt3() {
        return ext3;
    }

    public void setExt3(String ext3) {
        this.ext3 = ext3;
    }
    public String getExt4() {
        return ext4;
    }

    public void setExt4(String ext4) {
        this.ext4 = ext4;
    }
    public String getExt5() {
        return ext5;
    }

    public void setExt5(String ext5) {
        this.ext5 = ext5;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public static final String UVIEWSTR_ID = "uviewstr_id";

    public static final String UVIEW_ID = "uview_id";

    public static final String SRC_FIELD = "src_field";

    public static final String ENG_CD = "eng_cd";

    public static final String SRC_DATA_TYP = "src_data_typ";

    public static final String DATA_LEN = "data_len";

    public static final String ITEM_REMARK = "item_remark";

    public static final String SNO = "sno";

    public static final String EXT1 = "ext1";

    public static final String EXT2 = "ext2";

    public static final String EXT3 = "ext3";

    public static final String EXT4 = "ext4";

    public static final String EXT5 = "ext5";

    public static final String CREATE_TIME = "create_time";

    @Override
    public String toString() {
        return "ArchBusiUviewStrEx{" +
        "uviewstrId=" + uviewstrId +
        ", uviewId=" + uviewId +
        ", srcField=" + srcField +
        ", engCd=" + engCd +
        ", srcDataTyp=" + srcDataTyp +
        ", dataLen=" + dataLen +
        ", itemRemark=" + itemRemark +
        ", sno=" + sno +
        ", ext1=" + ext1 +
        ", ext2=" + ext2 +
        ", ext3=" + ext3 +
        ", ext4=" + ext4 +
        ", ext5=" + ext5 +
        ", createTime=" + createTime +
        ", ext6=" + ext6 +
        ", ext7=" + ext7 +
        ", ext8=" + ext8 +
        ", ext9=" + ext9 +
        ", ext10=" + ext10 +
        ", ext11=" + ext11 +
        ", ext12=" + ext12 +
        ", ext13=" + ext13 +
        ", ext14=" + ext14 +
        ", ext15=" + ext15 +
        ", ext16=" + ext16 +
        ", ext17=" + ext17 +
        ", ext18=" + ext18 +
        ", ext19=" + ext19 +
        ", ext20=" + ext20 +
        "}";
    }
}
