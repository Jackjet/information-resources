package com.digitalchina.resourcecatalog.db.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 组织机构
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
public class ArchOrgDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Integer orgId;

    /**
     * 组织机构名称
     */
    private String orgNm;

    /**
     * 机构编码
     */
    private String orgCd;

    /**
     * 联系人姓名
     */
    private String orgLinkman;

    /**
     * 联系人电话
     */
    private String orgTel;

    /**
     * FAX
     */
    private String orgFax;

    /**
     * EMAIL
     */
    private String orgMail;

    /**
     * 职能职责
     */
    private String orgDuty;

    /**
     * 通讯地址
     */
    private String addr;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 修改人
     */
    private String updater;

    /**
     * 创建时间
     */
    private Date crtDt;

    /**
     * 修改时间
     */
    private Date updateDt;

    /**
     * 简称
     */
    private String orgAlias;

    /**
     * 统一社会信用代码
     */
    private String socialCreditCd;

    /**
     * 显示顺序
     */
    private Integer dispalySn;

    /**
     * 父节点ID
     */
    private Integer parOrgId;

    @TableField(exist = false)
    private String busiNo;
    @TableField(exist = false)
    private String busiNm;

    @TableField(exist = false)
    private List children;

    @TableField(exist = false)
    private String orgNmPar;

    @TableField(exist = false)
    private String orgAliasPar;
    
    @TableField(exist = false)
    private Integer typId;
    
    @TableField(exist = false)
    private String typNm;

    public Integer getTypId() {
		return typId;
	}

	public void setTypId(Integer typId) {
		this.typId = typId;
	}

	public String getTypNm() {
		return typNm;
	}

	public void setTypNm(String typNm) {
		this.typNm = typNm;
	}

	public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getOrgNm() {
        return orgNm;
    }

    public void setOrgNm(String orgNm) {
        this.orgNm = orgNm;
    }

    public String getOrgCd() {
        return orgCd;
    }

    public void setOrgCd(String orgCd) {
        this.orgCd = orgCd;
    }

    public String getOrgLinkman() {
        return orgLinkman;
    }

    public void setOrgLinkman(String orgLinkman) {
        this.orgLinkman = orgLinkman;
    }

    public String getOrgTel() {
        return orgTel;
    }

    public void setOrgTel(String orgTel) {
        this.orgTel = orgTel;
    }

    public String getOrgFax() {
        return orgFax;
    }

    public void setOrgFax(String orgFax) {
        this.orgFax = orgFax;
    }

    public String getOrgMail() {
        return orgMail;
    }

    public void setOrgMail(String orgMail) {
        this.orgMail = orgMail;
    }

    public String getOrgDuty() {
        return orgDuty;
    }

    public void setOrgDuty(String orgDuty) {
        this.orgDuty = orgDuty;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public Date getCrtDt() {
        return crtDt;
    }

    public void setCrtDt(Date crtDt) {
        this.crtDt = crtDt;
    }

    public Date getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(Date updateDt) {
        this.updateDt = updateDt;
    }

    public String getOrgAlias() {
        return orgAlias;
    }

    public void setOrgAlias(String orgAlias) {
        this.orgAlias = orgAlias;
    }

    public String getSocialCreditCd() {
        return socialCreditCd;
    }

    public void setSocialCreditCd(String socialCreditCd) {
        this.socialCreditCd = socialCreditCd;
    }

    public Integer getDispalySn() {
        return dispalySn;
    }

    public void setDispalySn(Integer dispalySn) {
        this.dispalySn = dispalySn;
    }

    public Integer getParOrgId() {
        return parOrgId;
    }

    public void setParOrgId(Integer parOrgId) {
        this.parOrgId = parOrgId;
    }

    public static final String ORG_ID = "org_id";

    public static final String ORG_NM = "org_nm";

    public static final String ORG_CD = "org_cd";

    public static final String ORG_LINKMAN = "org_linkman";

    public static final String ORG_TEL = "org_tel";

    public static final String ORG_FAX = "org_fax";

    public static final String ORG_MAIL = "org_mail";

    public static final String ORG_DUTY = "org_duty";

    public static final String ADDR = "addr";

    public static final String CREATOR = "creator";

    public static final String UPDATER = "updater";

    public static final String CRT_DT = "crt_dt";

    public static final String UPDATE_DT = "update_dt";

    public static final String ORG_ALIAS = "org_alias";

    public static final String SOCIAL_CREDIT_CD = "social_credit_cd";

    public static final String DISPALY_SN = "dispaly_sn";

    public static final String PAR_ORG_ID = "par_org_id";

    @Override
    public String toString() {
        return "ArchOrg{" +
                "orgId=" + orgId +
                ", orgNm=" + orgNm +
                ", orgCd=" + orgCd +
                ", orgLinkman=" + orgLinkman +
                ", orgTel=" + orgTel +
                ", orgFax=" + orgFax +
                ", orgMail=" + orgMail +
                ", orgDuty=" + orgDuty +
                ", addr=" + addr +
                ", creator=" + creator +
                ", updater=" + updater +
                ", crtDt=" + crtDt +
                ", updateDt=" + updateDt +
                ", orgAlias=" + orgAlias +
                ", socialCreditCd=" + socialCreditCd +
                ", dispalySn=" + dispalySn +
                ", parOrgId=" + parOrgId +
                "}";
    }

    public List<ArchOrgDto> getChildren() {
        return children;
    }

    public void setChildren(List children) {
        this.children = children;
    }

    public String getBusiNo() {
        return busiNo;
    }

    public void setBusiNo(String busiNo) {
        this.busiNo = busiNo;
    }

    public String getBusiNm() {
        return busiNm;
    }

    public void setBusiNm(String busiNm) {
        this.busiNm = busiNm;
    }

    public String getOrgNmPar() {
        return orgNmPar;
    }

    public void setOrgNmPar(String orgNmPar) {
        this.orgNmPar = orgNmPar;
    }

    public String getOrgAliasPar() {
        return orgAliasPar;
    }

    public void setOrgAliasPar(String orgAliasPar) {
        this.orgAliasPar = orgAliasPar;
    }
}
