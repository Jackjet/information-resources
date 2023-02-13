package d1.project.d1project.system.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author kikki
 */
@Entity
@Table(name = "d1_menu_tree")
@ApiModel(value = "MenuTreeEntity", description = "菜单")
public class MenuTreeEntity extends BaseCreateAndUpdateEntity {

    @ApiModelProperty("路由地址")
    @Column(length = 200)
    private String path;

    @ApiModelProperty("名称")
    @Column(length = 100)
    private String name;

    @ApiModelProperty("父级id")
    @Column(length = 32)
    private String parentId;

    @ApiModelProperty("父级名称")
    @Column(length = 100)
    private String parentName;

    @ApiModelProperty("序列")
    @Column(length = 2)
    private int seq;

    @ApiModelProperty("id链接")
    @Column(length = 200)
    private String idLink;

    @ApiModelProperty("级别")
    @Column(length = 1)
    private int level;

    @ApiModelProperty("级别中文信息")
    @Column(length = 100)
    private String levelMsg;

    @ApiModelProperty("0 菜单，1 外链，2 按钮")
    @Column(length = 1)
    private int type;

    @ApiModelProperty("图标")
    @Column(length = 100)
    private String icon;

    @ApiModelProperty("是否是系统初始化 0 否、1 是")
    @Column(length = 10)
    private String hasSystem;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getIdLink() {
        return idLink;
    }

    public void setIdLink(String idLink) {
        this.idLink = idLink;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getHasSystem() {
        return hasSystem;
    }

    public void setHasSystem(String hasSystem) {
        this.hasSystem = hasSystem;
    }
}
