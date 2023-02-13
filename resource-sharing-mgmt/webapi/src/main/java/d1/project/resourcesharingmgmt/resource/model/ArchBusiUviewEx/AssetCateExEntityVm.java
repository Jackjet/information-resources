package d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewEx;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Column;

/**
 * 信息资源目录与信息类型关联
 *
 * @author zhengyang
 */
@ApiModel(value = "AssetCateExEntityVm", description = "信息资源目录与信息类型关联表")
public class AssetCateExEntityVm extends BaseCreateEntity {
    @ApiModelProperty("信息资源ID")
    @Column(length = 32)
    private String infoId;

    @ApiModelProperty("类型,1:基础信息 2:主题信息 3:部门信息")
    @Column(length = 4)
    private String type;

    @ApiModelProperty("所属类型ID")
    @Column(length = 32)
    private String typeid;

    public String getInfoId() {
        return infoId;
    }

    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }
}
