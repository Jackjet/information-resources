package d1.project.resourcesharingmgmt.system.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "d1_organization")
@ApiModel(value = "OrganizationEntity", description = "组织机构")
public class OrganizationEntity extends BaseCreateAndUpdateEntity {

    @ApiModelProperty("部门名称")
    @Column(length = 100)
    private String name;

    @ApiModelProperty("部门简称")
    @Column(length = 100)
    private String alias;

    @ApiModelProperty("序列")
    @Column(length = 2)
    private int seq;

    @ApiModelProperty("上级id")
    @Column(length = 32)
    private String parentId;

    @ApiModelProperty("上级名称")
    @Column(length = 100)
    private String parentName;

    @ApiModelProperty("级别")
    @Column(length = 1)
    private int level;

    @ApiModelProperty("级别中文信息")
    private String levelMsg;

    @ApiModelProperty("id链接")
    @Column(columnDefinition = "TEXT")
    private String idLink;

    @ApiModelProperty("id链接")
    @Column(columnDefinition = "TEXT")
    private String nameLink;

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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

    public String getNameLink() {
        return nameLink;
    }

    public void setNameLink(String nameLink) {
        this.nameLink = nameLink;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof OrganizationEntity) {
            OrganizationEntity entity = (OrganizationEntity) obj;
            if (entity.getId().equals(this.getId()) && entity.name.equals(this.name) && entity.parentId.equals(this.parentId) && entity.seq == this.seq && ((entity.alias == null && this.alias == null) || entity.alias.equals(this.alias)) )
            {
                return true;
            } else{
                return false;
            }
        }
        return false;
    }
}
