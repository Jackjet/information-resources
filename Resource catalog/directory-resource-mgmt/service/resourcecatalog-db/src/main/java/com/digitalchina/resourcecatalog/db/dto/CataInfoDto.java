package com.digitalchina.resourcecatalog.db.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.digitalchina.resourcecatalog.db.domain.ArchBusiUviewStrEx;
import com.digitalchina.resourcecatalog.db.domain.CataInfoItemTempRel;
import org.springframework.format.annotation.DateTimeFormat;

import com.digitalchina.resourcecatalog.db.domain.CataInfoTempTypeRel;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 信息资源目录(暂存表)
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public class CataInfoDto implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty("id")
    private Integer uviewId;

    /**
     * 关联资源分类
     */
    private String typeName="";
    /**
     * 信息资源分类
     */
    private String deptTypeName="";
    
    /**
     * 信息资源名称
     */
    @ApiModelProperty("信息资源名称")
    private String uviewNm="";

    /**
     * 信息资源代码
     */
    @ApiModelProperty("信息资源代码")
    private String uviewNo="";

    /**
     * 信息资源提供方机构ID,关联 表ID
     */
    @ApiModelProperty("信息资源提供方机构ID")
    private String belongTo;

    /**
     * 信息资源提供方机构名称
     */
    @ApiModelProperty("信息资源提供方机构名称")
    private String belongToName="";
    /**
     * 信息资源提供方机构代码
     */
    @ApiModelProperty("信息资源提供方机构代码")
    private String belongToCode="";

    /**
     * 信息资源提供方部门ID,关联 表ID
     */
    @ApiModelProperty("信息资源提供方部门ID")
    private Integer provOrgId;

    /**
     * 信息资源提供方部门代码
     */
    @ApiModelProperty("信息资源提供方部门代码")
    private String provOrgCode="";
    /**
     * 信息资源提供方部门名称
     */
    @ApiModelProperty("信息资源提供方部门名称")
    private String provOrgName="";

    /**
     * 信息资源格式分类
     */
    @ApiModelProperty("信息资源格式分类")
    private String mediaFmt="";
    
    /**
     * 信息资源格式分类名称
     */
    @ApiModelProperty("信息资源格式分类名称")
    private String mediaFmtName="";

    /**
     * 信息资源格式类型
     */
    @ApiModelProperty("信息资源格式类型")
    private String mediaFmtType="";

    /**
     * 信息资源格式类型名称
     */
    @ApiModelProperty("信息资源格式类型名称")
    private String mediaFmtTypeName="";
    /**
     * 更新周期
     */
    @ApiModelProperty("更新周期")
    private String updateCyc="";

    /**
     * 更新周期名称
     */
    @ApiModelProperty("更新周期名称")
    private String updateCycName="";
    /**
     * 发布日期
     */
    @ApiModelProperty("发布日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pubDt;

    /**
     * 信息资源摘要
     */
    @ApiModelProperty("信息资源摘要")
    private String uviewDesc="";

    /**
     * 共享类型，1无条件共享，2有条件共享，3不予共享
     */
    @ApiModelProperty("共享类型，1无条件共享，2有条件共享，3不予共享")
    private String shareLv="";
    
    /**
     * 共享类型名称
     */
    @ApiModelProperty("共享类型名称")
    private String shareLvName="";

    /**
     * 共享条件
     */
    @ApiModelProperty("共享条件")
    private String shareCondition="";

    /**
     * 是否想社会开放（0-否；1-是）
     */
    @ApiModelProperty("是否想社会开放（02-否；01-是）")
    private String pubSts="";

    /**
     * 开放条件
     */
    @ApiModelProperty("开放条件")
    private String pubCondition="";

    /**
     * 版本
     */
    @ApiModelProperty("版本")
    private String version="";

    /**
     * 状态，0草稿 1待审核 2未通过 3已发布
     */
    @ApiModelProperty("状态，0草稿 1待审核 2未通过 3已发布")
    private String auditStatus="";

    /**
     * 创建者
     */
    private String creator="";

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date crtDt;

    /**
     * 更新者
     */
    private String updater="";

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDt;
    
    /**
     * 逻辑删除 0-否 1-是
     */
    private Integer deleted;
    
    private List<ArchBusiUviewStrEx> itemList;

    public List<ArchBusiUviewStrEx> getItemList() {
		return itemList;
	}

	public void setItemList(List<ArchBusiUviewStrEx> itemList) {
		this.itemList = itemList;
	}

	public String getBelongToName() {
		return belongToName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public void setBelongToName(String belongToName) {
		this.belongToName = belongToName;
	}

	public String getProvOrgName() {
		return provOrgName;
	}

	public void setProvOrgName(String provOrgName) {
		this.provOrgName = provOrgName;
	}

	public String getMediaFmtName() {
		return mediaFmtName;
	}

	public void setMediaFmtName(String mediaFmtName) {
		this.mediaFmtName = mediaFmtName;
	}

	public String getMediaFmtTypeName() {
		return mediaFmtTypeName;
	}

	public void setMediaFmtTypeName(String mediaFmtTypeName) {
		this.mediaFmtTypeName = mediaFmtTypeName;
	}

	public String getUpdateCycName() {
		return updateCycName;
	}

	public void setUpdateCycName(String updateCycName) {
		this.updateCycName = updateCycName;
	}

	public String getShareLvName() {
		return shareLvName;
	}

	public void setShareLvName(String shareLvName) {
		this.shareLvName = shareLvName;
	}

    public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public Integer getUviewId() {
        return uviewId;
    }

    public void setUviewId(Integer uviewId) {
        this.uviewId = uviewId;
    }
    public String getUviewNm() {
        return uviewNm;
    }

    public void setUviewNm(String uviewNm) {
        this.uviewNm = uviewNm;
    }
    public String getUviewNo() {
        return uviewNo;
    }

    public void setUviewNo(String uviewNo) {
        this.uviewNo = uviewNo;
    }
    public String getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }
    public String getBelongToCode() {
        return belongToCode;
    }

    public void setBelongToCode(String belongToCode) {
        this.belongToCode = belongToCode;
    }
    public Integer getProvOrgId() {
        return provOrgId;
    }

    public void setProvOrgId(Integer provOrgId) {
        this.provOrgId = provOrgId;
    }
    public String getProvOrgCode() {
        return provOrgCode;
    }

    public void setProvOrgCode(String provOrgCode) {
        this.provOrgCode = provOrgCode;
    }
    public String getMediaFmt() {
        return mediaFmt;
    }

    public void setMediaFmt(String mediaFmt) {
        this.mediaFmt = mediaFmt;
    }
    public String getMediaFmtType() {
        return mediaFmtType;
    }

    public void setMediaFmtType(String mediaFmtType) {
        this.mediaFmtType = mediaFmtType;
    }
    public String getUpdateCyc() {
        return updateCyc;
    }

    public void setUpdateCyc(String updateCyc) {
        this.updateCyc = updateCyc;
    }
    public Date getPubDt() {
        return pubDt;
    }

    public void setPubDt(Date pubDt) {
        this.pubDt = pubDt;
    }
    public String getUviewDesc() {
        return uviewDesc;
    }

    public void setUviewDesc(String uviewDesc) {
        this.uviewDesc = uviewDesc;
    }
    public String getShareLv() {
        return shareLv;
    }

    public void setShareLv(String shareLv) {
        this.shareLv = shareLv;
    }
    public String getShareCondition() {
        return shareCondition;
    }

    public void setShareCondition(String shareCondition) {
        this.shareCondition = shareCondition;
    }
    public String getPubSts() {
        return pubSts;
    }

    public void setPubSts(String pubSts) {
        this.pubSts = pubSts;
    }
    public String getPubCondition() {
        return pubCondition;
    }

    public void setPubCondition(String pubCondition) {
        this.pubCondition = pubCondition;
    }
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
    public Date getCrtDt() {
        return crtDt;
    }

    public void setCrtDt(Date crtDt) {
        this.crtDt = crtDt;
    }
    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }
    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public String getDeptTypeName() {
		return deptTypeName;
	}

	public void setDeptTypeName(String deptTypeName) {
		this.deptTypeName = deptTypeName;
	}

	public static final String UVIEW_ID = "uview_id";

    public static final String UVIEW_NM = "uview_nm";

    public static final String UVIEW_NO = "uview_no";

    public static final String BELONG_TO = "belong_to";

    public static final String BELONG_TO_CODE = "belong_to_code";

    public static final String PROV_ORG_ID = "prov_org_id";

    public static final String PROV_ORG_CODE = "prov_org_code";

    public static final String MEDIA_FMT = "media_fmt";

    public static final String MEDIA_FMT_TYPE = "media_fmt_type";

    public static final String UPDATE_CYC = "update_cyc";

    public static final String PUB_DT = "pub_dt";

    public static final String UVIEW_DESC = "uview_desc";

    public static final String SHARE_LV = "share_lv";

    public static final String SHARE_CONDITION = "share_condition";

    public static final String PUB_STS = "pub_sts";

    public static final String PUB_CONDITION = "pub_condition";

    public static final String VERSION = "version";

    public static final String AUDIT_STATUS = "audit_status";

    public static final String CREATOR = "creator";

    public static final String CRT_DT = "crt_dt";

    public static final String UPDATER = "updater";

    public static final String UPDATE_DT = "update_dt";
    
    public static final String DELETED = "deleted";

    @Override
    public String toString() {
        return "CataInfoTemp{" +
        "uviewId=" + uviewId +
        ", uviewNm=" + uviewNm +
        ", uviewNo=" + uviewNo +
        ", belongTo=" + belongTo +
        ", belongToCode=" + belongToCode +
        ", provOrgId=" + provOrgId +
        ", provOrgCode=" + provOrgCode +
        ", mediaFmt=" + mediaFmt +
        ", mediaFmtType=" + mediaFmtType +
        ", updateCyc=" + updateCyc +
        ", pubDt=" + pubDt +
        ", uviewDesc=" + uviewDesc +
        ", shareLv=" + shareLv +
        ", shareCondition=" + shareCondition +
        ", pubSts=" + pubSts +
        ", pubCondition=" + pubCondition +
        ", version=" + version +
        ", auditStatus=" + auditStatus +
        ", creator=" + creator +
        ", crtDt=" + crtDt +
        ", updater=" + updater +
        ", updateDt=" + updateDt +
        ", deleted=" + deleted +
        "}";
    }
}
