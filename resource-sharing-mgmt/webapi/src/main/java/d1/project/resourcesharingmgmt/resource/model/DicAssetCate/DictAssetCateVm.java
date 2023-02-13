package d1.project.resourcesharingmgmt.resource.model.DicAssetCate;

import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * 目录分类管理
 *
 * @author zhengyang
 */
public class DictAssetCateVm{
    @ApiModelProperty("目录分类Id")
    private String typId;

    @ApiModelProperty("分类名称,1:基础信息 2:主题信息 3:部门信息")
    private String typNm;

    @ApiModelProperty("分类编码")
    private String typCd;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("状态,1、启用2、停用")
    private String status;

    @ApiModelProperty("排序字段")
    private Integer displaySn;

    @ApiModelProperty("详细编码")
    private String fullTypCd;

    @ApiModelProperty("类型,类，项，目")
    private String typType;

    @ApiModelProperty("父节点ID")
    private String parTypId;

    @ApiModelProperty("组织机构ID")
    private Integer orgId;

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
