package d1.project.resourcesharingmgmt.resource.model.DicAssetCateTree;

import java.util.List;

/**
 * @author maoyuying
 */
public class DictAssetCateTreeVm {
    private String typId;

    /**
     * 分类图标
     */
    private String imgUrl;
    /**
     * 分类名称
     */
    private String typNm;

    /**
     * 分类编码
     */
    private String typCd;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态（1、启用；2、停用）
     */
    private String status;

    /**
     * 显示序号
     */
    private Integer displaySn;

    /**
     * 详细编码（把上级编码拼接）
     */
    private String fullTypCd;

    /**
     * 类型（类、项、目）新增编辑时保存，便于查询
     */
    private String typType;
    /**
     * 父节点ID
     */
    private String parTypId;
    /**
     * 组织机构ID
     */
    private Integer orgId;
    /**
     * 子节点
     */
    private List<DictAssetCateTreeVm> children;

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

    public String getTypType() {
        return typType;
    }

    public void setTypType(String typType) {
        this.typType = typType;
    }

    public List<DictAssetCateTreeVm> getChildren() {
        return children;
    }

    public void setChildren(List<DictAssetCateTreeVm> children) {
        this.children = children;
    }
}
