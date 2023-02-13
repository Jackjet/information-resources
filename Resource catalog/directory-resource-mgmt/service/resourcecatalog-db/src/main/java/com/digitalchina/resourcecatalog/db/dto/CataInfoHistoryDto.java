package com.digitalchina.resourcecatalog.db.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.digitalchina.resourcecatalog.db.domain.CataInfoHistoryTypeRel;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 信息资源目录历史信息记录
 * </p>
 *
 * @author baokd
 * @since 2020-05-09
 */
public class CataInfoHistoryDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    /**
     * 信息资源ID
     */
    @NotNull(message = "信息资源ID不能为空")
    @ApiModelProperty("信息资源ID")
    private Integer uviewId;

    /**
     * 信息资源名称
     */
    @NotNull(message = "信息资源名称不能为空")
    @ApiModelProperty("信息资源名称")
    private String uviewNm;

    /**
     * 信息资源代码
     */
    @NotNull(message = "信息资源代码不能为空")
    @ApiModelProperty("信息资源代码")
    private String uviewNo;

    /**
     * 信息资源提供方机构ID,关联 表ID
     */
    @ApiModelProperty("信息资源提供方机构ID")
    private String belongTo;

    /**
     * 信息资源提供方机构名称
     */
    @ApiModelProperty("信息资源提供方机构名称")
    private String belongToName;
    /**
     * 信息资源提供方机构代码
     */
    @ApiModelProperty("信息资源提供方机构代码")
    private String belongToCode;

    /**
     * 信息资源提供方部门ID,关联 表ID
     */
    @NotNull(message = "信息资源提供方部门ID不能为空")
    @ApiModelProperty("信息资源提供方部门ID")
    private Integer provOrgId;

    /**
     * 信息资源提供方部门代码
     */
    @NotNull(message = "信息资源提供方部门代码不能为空")
    @ApiModelProperty("信息资源提供方部门代码")
    private String provOrgCode;
    /**
     * 信息资源提供方部门名称
     */
    @ApiModelProperty("信息资源提供方部门名称")
    private String provOrgName;

    /**
     * 信息资源格式分类
     */
    @NotNull(message = "信息资源格式分类不能为空")
    @ApiModelProperty("信息资源格式分类")
    private String mediaFmt;
    
    /**
     * 信息资源格式分类名称
     */
    @ApiModelProperty("信息资源格式分类名称")
    private String mediaFmtName;

    /**
     * 信息资源格式类型
     */
    @NotNull(message = "信息资源格式类型不能为空")
    @ApiModelProperty("信息资源格式类型")
    private String mediaFmtType;

    /**
     * 信息资源格式类型名称
     */
    @ApiModelProperty("信息资源格式类型名称")
    private String mediaFmtTypeName;
    /**
     * 更新周期
     */
    @NotNull(message = "更新周期不能为空")
    @ApiModelProperty("更新周期")
    private String updateCyc;

    /**
     * 更新周期名称
     */
    @ApiModelProperty("更新周期名称")
    private String updateCycName;
    /**
     * 发布日期
     */
    @NotNull(message = "发布日期不能为空")
    @ApiModelProperty("发布日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date pubDt;

    /**
     * 信息资源摘要
     */
    @NotNull(message = "信息资源摘要不能为空")
    @ApiModelProperty("信息资源摘要")
    private String uviewDesc;

    /**
     * 共享类型，1无条件共享，2有条件共享，3不予共享
     */
    @NotNull(message = "共享类型必选")
    @ApiModelProperty("共享类型，1无条件共享，2有条件共享，3不予共享")
    private String shareLv;
    
    /**
     * 共享类型名称
     */
    @ApiModelProperty("共享类型名称")
    private String shareLvName;

    /**
     * 共享条件
     */
    @NotNull(message = "共享条件不能为空")
    @ApiModelProperty("共享条件")
    private String shareCondition;

    /**
     * 是否想社会开放（0-否；1-是）
     */
    @NotNull(message = "是否想社会开放必选")
    @ApiModelProperty("是否想社会开放（0-否；1-是）")
    private String pubSts;

    /**
     * 开放条件
     */
    @NotNull(message = "开放条件不能为空")
    @ApiModelProperty("开放条件")
    private String pubCondition;

    /**
     * 版本
     */
    @ApiModelProperty("版本")
    private String version;

    /**
     * 状态，0草稿 1待审核 2未通过 3已发布
     */
    @ApiModelProperty("状态，0草稿 1待审核 2未通过 3已发布")
    private String auditStatus;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date crtDt;

    /**
     * 更新者
     */
    private String updater;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDt;
    
    /**
     * 是否当前版本 0-否 1-是
     */
    @ApiModelProperty("是否当前版本 0-否 1-是")
    private String isCurrent;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private Integer deleted;
    /**
     * 共享范围
     */

    @ApiModelProperty("共享范围")
    private String shareScope;
    /**
     * 数据范围
     */
    @ApiModelProperty("数据范围")
    private String dataScope;
    /**
     * 是否涉密 0-否 1-是
     */
    @ApiModelProperty("是否涉密")
    private Integer secret;
    /**
     * 是否涉密 0-否 1-是
     */
    @ApiModelProperty("是否关联权责")
    private Integer relBusi;

    @ApiModelProperty("权责清单补充说明")
    private String relBusiMsg;

    @ApiModelProperty("市级目录编码")
    private String cityCataCode;

    @ApiModelProperty("最后一次更新说明")
    private String lastUpdateDesc;

    @ApiModelProperty("更新绝限是否已读 0未读 1已读")
    private Integer updateLimitReaded;
    /**
     * 到期时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date limitDate;
    /**
     * 信息资源分类关联List
     */
    @ApiModelProperty("信息资源分类关联List")
    private List<CataInfoHistoryTypeRel> cataInfoHistoryTypeRelList;
    
    /**
     * 信息资源分类关联List
     */
    @ApiModelProperty("信息资源分类关联List(前端查询)")
    private List<CataInfoTempTypeRelDto> cataInfoHistoryTypeRelDtoList;
    
    /**
     * 多余信息项List
     */
    private List<String> surplusList;
    /**
     * 信息资源权责关联List
     */
    @ApiModelProperty("信息资源权责关联List")
    private List<Integer> relBusList;
    /**
     * 前端使用
     */
    @ApiModelProperty("内部部门")
    List<Integer> belongToList;

    public String getBelongToName() {
		return belongToName;
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

	public List<CataInfoTempTypeRelDto> getCataInfoHistoryTypeRelDtoList() {
		return cataInfoHistoryTypeRelDtoList;
	}

	public void setCataInfoHistoryTypeRelDtoList(List<CataInfoTempTypeRelDto> cataInfoHistoryTypeRelDtoList) {
		this.cataInfoHistoryTypeRelDtoList = cataInfoHistoryTypeRelDtoList;
	}

	public List<String> getSurplusList() {
		return surplusList;
	}

	public void setSurplusList(List<String> surplusList) {
		this.surplusList = surplusList;
	}

	public List<CataInfoHistoryTypeRel> getCataInfoHistoryTypeRelList() {
		return cataInfoHistoryTypeRelList;
	}

	public void setCataInfoHistoryTypeRelList(List<CataInfoHistoryTypeRel> cataInfoHistoryTypeRelList) {
		this.cataInfoHistoryTypeRelList = cataInfoHistoryTypeRelList;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
    public String getIsCurrent() {
        return isCurrent;
    }

    public void setIsCurrent(String isCurrent) {
        this.isCurrent = isCurrent;
    }
    public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
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

    public List<Integer> getRelBusList() {
        return relBusList;
    }

    public void setRelBusList(List<Integer> relBusList) {
        this.relBusList = relBusList;
    }

    public List<Integer> getBelongToList() {
        return belongToList;
    }

    public void setBelongToList(List<Integer> belongToList) {
        this.belongToList = belongToList;
    }

    public String getShareScope() {
        return shareScope;
    }

    public void setShareScope(String shareScope) {
        this.shareScope = shareScope;
    }

    public String getDataScope() {
        return dataScope;
    }

    public void setDataScope(String dataScope) {
        this.dataScope = dataScope;
    }

    public Integer getSecret() {
        return secret;
    }

    public void setSecret(Integer secret) {
        this.secret = secret;
    }

    public Integer getRelBusi() {
        return relBusi;
    }

    public void setRelBusi(Integer relBusi) {
        this.relBusi = relBusi;
    }

    public String getRelBusiMsg() {
        return relBusiMsg;
    }

    public void setRelBusiMsg(String relBusiMsg) {
        this.relBusiMsg = relBusiMsg;
    }

    public String getCityCataCode() {
        return cityCataCode;
    }

    public void setCityCataCode(String cityCataCode) {
        this.cityCataCode = cityCataCode;
    }

    public String getLastUpdateDesc() {
        return lastUpdateDesc;
    }

    public void setLastUpdateDesc(String lastUpdateDesc) {
        this.lastUpdateDesc = lastUpdateDesc;
    }

    public Integer getUpdateLimitReaded() {
        return updateLimitReaded;
    }

    public void setUpdateLimitReaded(Integer updateLimitReaded) {
        this.updateLimitReaded = updateLimitReaded;
    }

    public Date getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(Date limitDate) {
        this.limitDate = limitDate;
    }

    public static final String ID = "id";

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

    public static final String IS_CURRENT = "is_current";

    public static final String DELETED = "deleted";

    public static final String CREATOR = "creator";

    public static final String CRT_DT = "crt_dt";

    public static final String UPDATER = "updater";

    public static final String UPDATE_DT = "update_dt";

    @Override
    public String toString() {
        return "CataInfoHistory{" +
        "id=" + id +
        ", uviewId=" + uviewId +
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
        ", isCurrent=" + isCurrent +
        ", deleted=" + deleted +
        ", creator=" + creator +
        ", crtDt=" + crtDt +
        ", updater=" + updater +
        ", updateDt=" + updateDt +
        "}";
    }
}
