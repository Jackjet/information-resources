package d1.project.resourcesharingmgmt.resource.model.view;

import d1.framework.webapi.annotation.ApiModelProperty;
import org.hibernate.annotations.Subselect;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.util.Calendar;

/**
 * 资源目录视图
 *
 * @author maoyuying
 */
@Entity
@Immutable
@Subselect("SELECT * FROM ( SELECT " +
        "d1_arch_busi_uview_ex.id as id," +
        "d1_arch_busi_uview_ex.create_by_id as create_by_id," +
        "d1_arch_busi_uview_ex.create_time as create_time," +
        "d1_arch_busi_uview_ex.update_by_id as update_by_id," +
        "d1_arch_busi_uview_ex.update_time as update_time," +
        "d1_arch_busi_uview_ex.audit_status as audit_status," +
        "d1_arch_busi_uview_ex.belong_to as belong_to," +
        "d1_arch_busi_uview_ex.belong_to_code as belong_to_code," +
        "d1_arch_busi_uview_ex.media_fmt as media_fmt," +
        "d1_arch_busi_uview_ex.media_fmt_type as media_fmt_type," +
        "d1_arch_busi_uview_ex.prov_org_code as prov_org_code," +
        "d1_arch_busi_uview_ex.prov_org_id as prov_org_id," +
        "d1_arch_busi_uview_ex.pub_condition as pub_condition," +
        "d1_arch_busi_uview_ex.pub_dt as pub_dt," +
        "d1_arch_busi_uview_ex.pub_sts as pub_sts," +
        "d1_arch_busi_uview_ex.share_condition as share_condition," +
        "d1_arch_busi_uview_ex.share_lv as share_lv," +
        "d1_arch_busi_uview_ex.status as status," +
        "d1_arch_busi_uview_ex.update_cyc as update_cyc," +
        "d1_arch_busi_uview_ex.uview_desc as uview_desc," +
        "d1_arch_busi_uview_ex.uview_id as uview_id," +
        "d1_arch_busi_uview_ex.uview_nm as uview_nm," +
        "d1_arch_busi_uview_ex.uview_no as uview_no," +
        "d1_arch_busi_uview_ex.version as version," +
        "d1_arch_busi_uview_ex.visits_count as visits_count," +
        "d1_arch_busi_uview_ex.hook_status as hook_status," +
        "d1_asset_cate_ex.info_id as info_id," +
        "d1_asset_cate_ex.typ as typ," +
        "d1_asset_cate_ex.typ_id as typ_id," +
        "d1_dict_asset_cate.typ_nm as typ_nm," +
        "d1_organization.name as name, " +
        "0 as is_focus " +
        "FROM d1_arch_busi_uview_ex " +
        "LEFT JOIN d1_asset_cate_ex ON d1_asset_cate_ex.info_id = d1_arch_busi_uview_ex.uview_id " +
        "LEFT JOIN d1_dict_asset_cate ON d1_asset_cate_ex.typ_id= d1_dict_asset_cate.typ_id " +
        "LEFT JOIN d1_organization ON d1_organization.id = d1_arch_busi_uview_ex.prov_org_id " +
        "LEFT JOIN d1_my_focus_info ON d1_my_focus_info.uview_id = d1_arch_busi_uview_ex.uview_id ) AS C")
public class ArchBusiUviewExView {
    @Id
    private String id;

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

    @ApiModelProperty("下架状态,0未下架 1已下架")
    private String status;

    @ApiModelProperty("状态,0草稿 1待审核 2未通过 3已发布 ")
    private String auditStatus;

    @ApiModelProperty("访问量")
    private int visitsCount;

    @ApiModelProperty("创建人Id")
    private String createById;
    @ApiModelProperty(
            value = "创建时间",
            example = "2018-01-01 01:01:01"
    )
    private Calendar createTime;

    @ApiModelProperty("最后修改人Id")
    private String updateById;

    @ApiModelProperty(
            value = "最后修改时间",
            example = "2018-01-01 01:01:01"
    )
    private Calendar updateTime;

    @ApiModelProperty("信息资源ID")
    private String infoId;

    @ApiModelProperty("类型,1:基础信息 2:主题信息 3:部门信息")
    private String typ;

    @ApiModelProperty("所属类型ID")
    private String typId;

    @ApiModelProperty("分类名称")
    private String typNm;

    @ApiModelProperty("机构名称")
    private String name;

    @ApiModelProperty("是否关注,0未关注，1已关注")
    private long isFocus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    public String getUpdateById() {
        return updateById;
    }

    public void setUpdateById(String updateById) {
        this.updateById = updateById;
    }

    public Calendar getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Calendar updateTime) {
        this.updateTime = updateTime;
    }

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public String getTypId() {
        return typId;
    }

    public void setTypId(String typId) {
        this.typId = typId;
    }

    public String getTypNm() {
        return typNm;
    }

    public void setTypNm(String typNm) {
        this.typNm = typNm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getIsFocus() {
        return isFocus;
    }

    public void setIsFocus(long isFocus) {
        this.isFocus = isFocus;
    }
}
