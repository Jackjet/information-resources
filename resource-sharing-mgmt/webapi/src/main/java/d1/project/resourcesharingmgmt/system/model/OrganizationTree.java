package d1.project.resourcesharingmgmt.system.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "OrganizationTreeVm", description = "组织机构树")
public class OrganizationTree extends BaseEntity {

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

    @ApiModelProperty("子集")
    private List<OrganizationTree> children;

    public OrganizationTree() {
        children = new ArrayList<>();
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getLevelMsg() {
        return levelMsg;
    }

    public void setLevelMsg(String levelMsg) {
        this.levelMsg = levelMsg;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getIdLink() {
        return idLink;
    }

    public void setIdLink(String idLink) {
        this.idLink = idLink;
    }

    public List<OrganizationTree> getChildren() {
        return children;
    }

    public void setChildren(List<OrganizationTree> children) {
        this.children = children;
    }
}
