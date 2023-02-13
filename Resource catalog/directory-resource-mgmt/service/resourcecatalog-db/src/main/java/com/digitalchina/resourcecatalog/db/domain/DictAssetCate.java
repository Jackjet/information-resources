package com.digitalchina.resourcecatalog.db.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 目录分类管理
 * </p>
 *
 * @author baokd
 * @since 2020-05-12
 */
public class DictAssetCate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "typ_id", type = IdType.AUTO)
    private Integer typId;

    /**
     * 分类名称
     */
    @NotNull(message = "分类名称不能为空")
    @ApiModelProperty("分类名称")
    private String typNm;

    /**
     * 分类编码
     */
    @NotNull(message = "分类编码不能为空")
    @ApiModelProperty("分类编码")
    private String typCd;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 状态（1、启用；2、停用）
     */
    @ApiModelProperty("状态1、启用2、停用")
    private String status;

    /**
     * 显示序号
     */
    @ApiModelProperty("排序字段")
    private Integer displaySn;

    /**
     * 详细编码（把上级编码拼接）
     */
    @ApiModelProperty("详细编码")
    private String fullTypCd;

    /**
     * 类型（类、项、目）新增编辑时保存，便于查询
     */
    @ApiModelProperty("类型（类，项，目）")
    private String typType;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private LocalDateTime crtDt;

    /**
     * 创建者
     */
    @ApiModelProperty("创建者")
    private String creator;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private LocalDateTime updateDt;

    /**
     * 更新者
     */
    @ApiModelProperty("更新者")
    private String updater;

    /**
     * 父节点ID
     */
    @ApiModelProperty("父节点ID")
    private Integer parTypId;

    /**
     * 父节点ID
     */
    @ApiModelProperty("组织机构ID")
    private Integer orgId;


    @TableField(exist = false)
    private List<DictAssetCate> children;

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
    public String getTypCd() {
        return typCd;
    }

    public void setTypCd(String typCd) {
        this.typCd = typCd;
    }
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Integer getDisplaySn() {
        return displaySn;
    }

    public void setDisplaySn(Integer displaySn) {
        this.displaySn = displaySn;
    }
    public String getFullTypCd() {
        return fullTypCd;
    }

    public void setFullTypCd(String fullTypCd) {
        this.fullTypCd = fullTypCd;
    }
    public String getTypType() {
        return typType;
    }

    public void setTypType(String typType) {
        this.typType = typType;
    }
    public LocalDateTime getCrtDt() {
        return crtDt;
    }

    public void setCrtDt(LocalDateTime crtDt) {
        this.crtDt = crtDt;
    }
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
    public LocalDateTime getUpdateDt() {
        return updateDt;
    }

    public void setUpdateDt(LocalDateTime updateDt) {
        this.updateDt = updateDt;
    }
    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }
    public Integer getParTypId() {
        return parTypId;
    }

    public void setParTypId(Integer parTypId) {
        this.parTypId = parTypId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public static final String TYP_ID = "typ_id";

    public static final String TYP_NM = "typ_nm";

    public static final String TYP_CD = "typ_cd";

    public static final String REMARK = "remark";

    public static final String STATUS = "status";

    public static final String DISPLAY_SN = "display_sn";

    public static final String FULL_TYP_CD = "full_typ_cd";

    public static final String TYP_TYPE = "typ_type";

    public static final String CRT_DT = "crt_dt";

    public static final String CREATOR = "creator";

    public static final String UPDATE_DT = "update_dt";

    public static final String UPDATER = "updater";

    public static final String PAR_TYP_ID = "par_typ_id";

    public static final String ORG_ID = "org_id";
    @Override
    public String toString() {
        return "DictAssetCate{" +
        "typId=" + typId +
        ", typNm=" + typNm +
        ", typCd=" + typCd +
        ", remark=" + remark +
        ", status=" + status +
        ", displaySn=" + displaySn +
        ", fullTypCd=" + fullTypCd +
        ", typType=" + typType +
        ", crtDt=" + crtDt +
        ", creator=" + creator +
        ", updateDt=" + updateDt +
        ", updater=" + updater +
        ", parTypId=" + parTypId +
        ", orgId=" + orgId +
        "}";
    }

    public List<DictAssetCate> getChildren() {
        return children;
    }

    public void setChildren(List<DictAssetCate> children) {
        this.children = children;
    }
}
