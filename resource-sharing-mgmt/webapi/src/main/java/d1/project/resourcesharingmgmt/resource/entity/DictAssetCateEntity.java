package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

/**
 * 目录分类管理
 *
 * @author zhengyang
 */
@Entity
@Table(name = "d1_dict_asset_cate")
@ApiModel(value = "DictAssetCateEntity", description = "目录分类管理")
public class DictAssetCateEntity extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("目录分类Id")
    @Column(length = 32)
    private String typId;

    @ApiModelProperty("分类图标")
    @Column(length = 200)
    private String imgUrl;

    @ApiModelProperty("分类名称,1:基础信息 2:主题信息 3:部门信息")
    @Column(length = 255)
    private String typNm;

    @ApiModelProperty("分类编码")
    @Column(length = 32)
    private String typCd;

    @ApiModelProperty("备注")
    @Column(columnDefinition = "TEXT")
    private String remark;

    @ApiModelProperty("状态,1、启用2、停用")
    @Column(length = 4)
    private String status;

    @ApiModelProperty("排序字段")
    private Integer displaySn;

    @ApiModelProperty("详细编码")
    @Column(length = 100)
    private String fullTypCd;

    @ApiModelProperty("类型,类，项，目")
    @Column(length = 10)
    private String typType;

    @ApiModelProperty("父节点ID")
    @Column(length = 32)
    private String parTypId;

    @ApiModelProperty("组织机构ID")
    @Column(length = 10)
    private Integer orgId;

    public String getTypId() {
        return typId;
    }

    public void setTypId(String typId) {
        this.typId = typId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public String getParTypId() {
        return parTypId;
    }

    public void setParTypId(String parTypId) {
        this.parTypId = parTypId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }
}
