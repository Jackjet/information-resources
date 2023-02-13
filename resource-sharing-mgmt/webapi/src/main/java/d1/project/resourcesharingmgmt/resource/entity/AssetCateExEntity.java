package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 信息资源目录与信息类型关联
 *
 * @author zhengyang
 */
@Entity
@Table(name = "d1_asset_cate_ex")
@ApiModel(value = "AssetCateExEntity", description = "信息资源目录与信息类型关联表")
public class AssetCateExEntity extends BaseCreateEntity {
    @ApiModelProperty("信息资源ID")
    @Column(length = 32)
    private String infoId;

    @ApiModelProperty("类型,1:基础信息 2:主题信息 3:部门信息")
    @Column(length = 4)
    private String typ;

    @ApiModelProperty("所属类型ID")
    @Column(length = 32)
    private String typId;

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
}
