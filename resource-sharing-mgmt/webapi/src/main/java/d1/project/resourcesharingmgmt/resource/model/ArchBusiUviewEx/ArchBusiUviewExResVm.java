package d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewEx;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import java.util.Calendar;

@ApiModel(value = "ArchBusiUviewExResVm", description = "信息资源目录网页端返回数据")
public class ArchBusiUviewExResVm extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("信息资源ID")
    private String uviewId;

    @ApiModelProperty("信息资源名称")
    private String uviewNm;

    @ApiModelProperty("信息资源代码")
    private String uviewNo;

    @ApiModelProperty("信息资源提供方机构ID")
    private String belongTo;

    @ApiModelProperty("信息资源提供方机构代码")
    private String belongToCode;

    @ApiModelProperty("信息资源提供方部门ID")
    private String provOrgId;

    @ApiModelProperty("信息资源提供方部门代码")
    private String provOrgCode;

    @ApiModelProperty("信息资源格式分类")
    private String mediaFmt;

    @ApiModelProperty("信息资源格式类型")
    private String mediaFmtType;

    @ApiModelProperty("更新周期")
    private String updateCyc;

    @ApiModelProperty("发布日期")
    private Calendar pubDt;

    @ApiModelProperty("信息资源摘要")
    private String uviewDesc;

    @ApiModelProperty("共享类型,1无条件共享，2有条件共享，3不予共享")
    private String shareLv;

    @ApiModelProperty("共享条件")
    private String shareCondition;

    @ApiModelProperty("是否想社会开放,0:否；1:是")
    private String pubSts;

    @ApiModelProperty("开放条件")
    private String pubCondition;

    @ApiModelProperty("版本")
    private String version;

    @ApiModelProperty("挂接状态,0未挂接 1已挂接")
    private String hookStatus;

    @ApiModelProperty("文件挂接状态,0未挂接 1已挂接")
    private String fileHookStatus;

    @ApiModelProperty("数据库挂接状态,0未挂接 1已挂接")
    private String dataHookStatus;

    @ApiModelProperty("下架状态,0未下架 1已下架")
    private String status;

    @ApiModelProperty("状态,0草稿 1待审核 2未通过 3已发布 ")
    private String auditStatus;

    @ApiModelProperty("访问量")
    private int visitsCount;

//    @ApiModelProperty("信息资源ID")
//    private String infoId;
//
//    @ApiModelProperty("类型,1:基础信息 2:主题信息 3:部门信息")
//    private String typ;
//
//    @ApiModelProperty("所属类型ID")
//    private String typId;
//
//    @ApiModelProperty("分类名称")
//    private String typNm;
//
//    @ApiModelProperty("机构名称")
//    private String name;

    @ApiModelProperty("是否关注,0未关注，1已关注")
    private long isFocus;

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

    public long getIsFocus() {
        return isFocus;
    }

    public void setIsFocus(long isFocus) {
        this.isFocus = isFocus;
    }
}
