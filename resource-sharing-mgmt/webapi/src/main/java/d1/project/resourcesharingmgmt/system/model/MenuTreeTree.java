package d1.project.resourcesharingmgmt.system.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kikki
 */
@ApiModel(value = "MenuTreeTreeVm", description = "菜单")
public class MenuTreeTree extends MenuTreeBase {

    @ApiModelProperty("id")
    @NotBlank(message = "id不可为空")
    private String id;

    @ApiModelProperty("父级id")
    private String parentId;

    @ApiModelProperty("父级名称")
    private String parentName;

    @ApiModelProperty("序列")
    private int seq;

    @ApiModelProperty("级别中文信息")
    private String levelMsg;

    @ApiModelProperty("子集")
    private List<MenuTreeTree> children;

    @ApiModelProperty("是否是系统初始化 0 否、1 是")
    private String hasSystem;

    public MenuTreeTree() {
        children = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getLevelMsg() {
        return levelMsg;
    }

    public void setLevelMsg(String levelMsg) {
        this.levelMsg = levelMsg;
    }

    public List<MenuTreeTree> getChildren() {
        return children;
    }

    public void setChildren(List<MenuTreeTree> children) {
        this.children = children;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getHasSystem() {
        return hasSystem;
    }

    public void setHasSystem(String hasSystem) {
        this.hasSystem = hasSystem;
    }
}
