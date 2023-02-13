package d1.project.resourcesharingmgmt.resource.model.DicAssetCateTree;

import d1.framework.webapi.annotation.ApiModelProperty;

import java.util.List;

/**
 * @author maoyuying
 */
public class OrganizationTreeVm {
    @ApiModelProperty("部门id")
    private String id;

    @ApiModelProperty("部门名称")
    private String name;

    @ApiModelProperty("序列")
    private int seq;

    @ApiModelProperty("上级id")
    private String parentId;

    @ApiModelProperty("上级名称")
    private String parentName;

    @ApiModelProperty("级别")
    private int level;

    @ApiModelProperty("级别中文信息")
    private String levelMsg;

    @ApiModelProperty("id链接")
    private String idLink;

    @ApiModelProperty("id链接")
    private String nameLink;

    private List<DictAssetCateTreeVm> children;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLevelMsg() {
        return levelMsg;
    }

    public void setLevelMsg(String levelMsg) {
        this.levelMsg = levelMsg;
    }

    public String getIdLink() {
        return idLink;
    }

    public void setIdLink(String idLink) {
        this.idLink = idLink;
    }

    public String getNameLink() {
        return nameLink;
    }

    public void setNameLink(String nameLink) {
        this.nameLink = nameLink;
    }

    public List<DictAssetCateTreeVm> getChildren() {
        return children;
    }

    public void setChildren(List<DictAssetCateTreeVm> children) {
        this.children = children;
    }
}
