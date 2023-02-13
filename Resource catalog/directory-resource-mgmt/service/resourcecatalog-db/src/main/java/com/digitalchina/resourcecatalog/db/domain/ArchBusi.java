package com.digitalchina.resourcecatalog.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 权责清单
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
public class ArchBusi implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer busiId;

    /**
     * 权责清单编码
     */
    private String busiNo;

    /**
     * 权责清单名称
     */
    private String busiNm;

    /**
     * 部门id
     */
    private Integer deptId;

    /**
     * 服务对象 与dict关联
     */
    private String serviceObj;

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

    private Integer pId;

    @TableField(exist = false)
    private List children;

    /***
     * 服务对象名称
     */
    @TableField(exist = false)
    private String serviceObjName;

    @TableField(exist = false)
    private List<ArchAppSys> appList;

    @TableField(exist = false)
    private String orgNm;

    @TableField(exist = false)
    private String orgDuty;

    @TableField(exist = false)
    private String pBusiNm;
    @TableField(exist = false)
    private Integer belongTo;

    /**
     * 类别
     */
    private String busiType;

    public Integer getBusiId() {
        return busiId;
    }

    public void setBusiId(Integer busiId) {
        this.busiId = busiId;
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

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getServiceObj() {
        return serviceObj;
    }

    public void setServiceObj(String serviceObj) {
        this.serviceObj = serviceObj;
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

    public static final String BUSI_ID = "busi_id";

    public static final String BUSI_NO = "busi_no";

    public static final String BUSI_NM = "busi_nm";

    public static final String DEPT_ID = "dept_id";

    public static final String P_ID = "p_id";

    public static final String SERVICE_OBJ = "service_obj";

    public static final String REMARK = "remark";

    public static final String CREATED_BY = "created_by";

    public static final String CREATED_TIME = "created_time";

    public static final String UPDATED_BY = "updated_by";

    public static final String UPDATED_TIME = "updated_time";

    public static final String BUSI_TYPE = "busi_type";

    @Override
    public String toString() {
        return "ArchBusi{" +
                "busiId=" + busiId +
                ", busiNo='" + busiNo + '\'' +
                ", busiNm='" + busiNm + '\'' +
                ", deptId=" + deptId +
                ", serviceObj='" + serviceObj + '\'' +
                ", remark='" + remark + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdTime=" + createdTime +
                ", updatedBy='" + updatedBy + '\'' +
                ", updatedTime=" + updatedTime +
                ", pId=" + pId +
                ", children=" + children +
                ", serviceObjName='" + serviceObjName + '\'' +
                ", appList=" + appList +
                ", orgNm='" + orgNm + '\'' +
                ", orgDuty='" + orgDuty + '\'' +
                ", pBusiNm='" + pBusiNm + '\'' +
                ", belongTo=" + belongTo +
                ", busiType='" + busiType + '\'' +
                '}';
    }

    public List getChildren() {
        return children;
    }

    public void setChildren(List children) {
        this.children = children;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getServiceObjName() {
        return serviceObjName;
    }

    public void setServiceObjName(String serviceObjName) {
        this.serviceObjName = serviceObjName;
    }

    public List<ArchAppSys> getAppList() {
        return appList;
    }

    public void setAppList(List<ArchAppSys> appList) {
        this.appList = appList;
    }

    public String getOrgNm() {
        return orgNm;
    }

    public void setOrgNm(String orgNm) {
        this.orgNm = orgNm;
    }

    public String getOrgDuty() {
        return orgDuty;
    }

    public void setOrgDuty(String orgDuty) {
        this.orgDuty = orgDuty;
    }

    public String getpBusiNm() {
        return pBusiNm;
    }

    public void setpBusiNm(String pBusiNm) {
        this.pBusiNm = pBusiNm;
    }

    public Integer getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(Integer belongTo) {
        this.belongTo = belongTo;
    }

    public String getBusiType() {
        return busiType;
    }

    public void setBusiType(String busiType) {
        this.busiType = busiType;
    }

    public static String getREMARK() {
        return REMARK;
    }
}
