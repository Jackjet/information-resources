package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.*;
import java.util.Calendar;

/**
 * 信息资源目录
 *
 * @author zhengyang
 */
@Entity
@Table(name = "d1_arch_busi_uview_ex")
@ApiModel(value = "ArchBusiUviewExEntity", description = "信息资源目录")
public class ArchBusiUviewExEntity extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("信息资源ID")
    @Column(length = 32)
    private String uviewId;

    @ApiModelProperty("信息资源名称")
    @Column(length = 225)
    private String uviewNm;

    @ApiModelProperty("信息资源代码")
    @Column(length = 100)
    private String uviewNo;

    @ApiModelProperty("信息资源提供方机构ID")
    @Column(length = 500)
    private String belongTo;

    @ApiModelProperty("信息资源提供方机构代码")
    @Column(length = 500)
    private String belongToCode;

    @ApiModelProperty("信息资源提供方部门ID")
    @Column(length = 32)
    private String provOrgId;

    @ApiModelProperty("信息资源提供方部门代码")
    @Column(length = 100)
    private String provOrgCode;

    @ApiModelProperty("信息资源格式分类")
    @Column(length = 100)
    private String mediaFmt;

    @ApiModelProperty("信息资源格式类型")
    @Column(length = 100)
    private String mediaFmtType;

    @ApiModelProperty("更新周期")
    @Column(length = 100)
    private String updateCyc;

    @ApiModelProperty("发布日期")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar pubDt;

    @ApiModelProperty("信息资源摘要")
    @Column(columnDefinition = "TEXT")
    private String uviewDesc;

    @ApiModelProperty("共享类型,01无条件共享，02有条件共享，03不予共享")
    @Column(length = 4)
    private String shareLv;

    @ApiModelProperty("共享条件")
    @Column(length = 100)
    private String shareCondition;

    @ApiModelProperty("是否向社会开放,0:否；1:是")
    @Column(length = 4)
    private String pubSts;

    @ApiModelProperty("开放条件")
    @Column(length = 1000)
    private String pubCondition;

    @ApiModelProperty("版本")
    @Column(length = 10)
    private String version;

    @ApiModelProperty("API挂接状态,0未挂接 1已挂接")
    @Column(length = 4)
    private String hookStatus;

    @ApiModelProperty("文件挂接状态,0未挂接 1已挂接")
    @Column(length = 4)
    private String fileHookStatus;

    @ApiModelProperty("数据库挂接状态,0未挂接 1已挂接")
    @Column(length = 4)
    private String dataHookStatus;

    @ApiModelProperty("下架状态,0未下架 1已下架")
    @Column(length = 4)
    private String status;

    @ApiModelProperty("状态,0草稿 1待审核 2未通过 3已发布 ")
    @Column(length = 4)
    private String auditStatus;

    @ApiModelProperty("访问量")
    @Column(columnDefinition="int4 default 0")
    private int visitsCount;

    @ApiModelProperty("逻辑删除 0-否 1-是")
    private Integer deleted;

    @ApiModelProperty("挂接日期")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar hookTime;

    public String getUviewId() {
        return uviewId;
    }

    public void setUviewId(String uviewId) {
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

    public String getProvOrgId() {
        return provOrgId;
    }

    public void setProvOrgId(String provOrgId) {
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

    public Calendar getPubDt() {
        return pubDt;
    }

    public void setPubDt(Calendar pubDt) {
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

    public String getHookStatus() {
        return hookStatus;
    }

    public void setHookStatus(String hookStatus) {
        this.hookStatus = hookStatus;
    }

    public String getFileHookStatus() {
        return fileHookStatus;
    }

    public void setFileHookStatus(String fileHookStatus) {
        this.fileHookStatus = fileHookStatus;
    }

    public String getDataHookStatus() {
        return dataHookStatus;
    }

    public void setDataHookStatus(String dataHookStatus) {
        this.dataHookStatus = dataHookStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public int getVisitsCount() {
        return visitsCount;
    }

    public void setVisitsCount(int visitsCount) {
        this.visitsCount = visitsCount;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Calendar getHookTime() {
        return hookTime;
    }

    public void setHookTime(Calendar hookTime) {
        this.hookTime = hookTime;
    }
}
