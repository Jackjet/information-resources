package com.digitalchina.resourcecatalog.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 应用系统信息
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
public class ArchAppSys implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer appsysId;

    /**
     * 部门ID
     */
    private Integer provOrgId;

    /**
     * 下设机构
     */
    private Integer belongTo;

    /**
     * 应用系统代码
     */
    private String appsysNo;

    /**
     * 应用系统名称
     */
    private String appsysNm;

    /**
     * 建设性质
     */
    private String nature;

    /**
     * 应用系统简介
     */
    private String appsysDesc;

    /**
     * 部署位置
     */
    private String position;

    /**
     * 接入网络类型
     */
    private String netType;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人ID
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private Date createdTime;

    /**
     * 更新人ID
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private Date updatedTime;

    @TableField(exist = false)
    private List<ArchBusi> busis;

    public Integer getAppsysId() {
        return appsysId;
    }

    public void setAppsysId(Integer appsysId) {
        this.appsysId = appsysId;
    }

    public Integer getProvOrgId() {
        return provOrgId;
    }

    public void setProvOrgId(Integer provOrgId) {
        this.provOrgId = provOrgId;
    }

    public Integer getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(Integer belongTo) {
        this.belongTo = belongTo;
    }

    public String getAppsysNo() {
        return appsysNo;
    }

    public void setAppsysNo(String appsysNo) {
        this.appsysNo = appsysNo;
    }

    public String getAppsysNm() {
        return appsysNm;
    }

    public void setAppsysNm(String appsysNm) {
        this.appsysNm = appsysNm;
    }

    public String getNature() {
        return nature;
    }

    public void setNature(String nature) {
        this.nature = nature;
    }

    public String getAppsysDesc() {
        return appsysDesc;
    }

    public void setAppsysDesc(String appsysDesc) {
        this.appsysDesc = appsysDesc;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getNetType() {
        return netType;
    }

    public void setNetType(String netType) {
        this.netType = netType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public static final String APPSYS_ID = "appsys_id";

    public static final String PROV_ORG_ID = "prov_org_id";

    public static final String BELONG_TO = "belong_to";

    public static final String APPSYS_NO = "appsys_no";

    public static final String APPSYS_NM = "appsys_nm";

    public static final String NATURE = "nature";

    public static final String APPSYS_DESC = "appsys_desc";

    public static final String POSITION = "position";

    public static final String NET_TYPE = "net_type";

    public static final String REMARK = "remark";

    public static final String CREATED_BY = "created_by";

    public static final String CREATED_TIME = "created_time";

    public static final String UPDATED_BY = "updated_by";

    public static final String UPDATED_TIME = "updated_time";

    @Override
    public String toString() {
        return "ArchAppSys{" +
                "appsysId=" + appsysId +
                ", provOrgId=" + provOrgId +
                ", belongTo=" + belongTo +
                ", appsysNo=" + appsysNo +
                ", appsysNm=" + appsysNm +
                ", nature=" + nature +
                ", appsysDesc=" + appsysDesc +
                ", position=" + position +
                ", netType=" + netType +
                ", remark=" + remark +
                ", createdBy=" + createdBy +
                ", createdTime=" + createdTime +
                ", updatedBy=" + updatedBy +
                ", updatedTime=" + updatedTime +
                "}";
    }

    public List<ArchBusi> getBusis() {
        return busis;
    }

    public void setBusis(List<ArchBusi> busis) {
        this.busis = busis;
    }
}
