package d1.project.resourcesharingmgmt.resource.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 资源目录字典
 *
 * @author zhengyang
 */
@Entity
@Table(name = "d1_asset_dict")
@ApiModel(value = "AssetDictEntity", description = "资源目录字典")
public class AssetDictEntity extends BaseEntity {
    @ApiModelProperty("字典名称")
    private String name;

    @ApiModelProperty("字典值")
    private String value;

    @ApiModelProperty("字典分类")
    @Column(length = 255)
    private String type;

    @ApiModelProperty("pid")
    private Integer pid;

    @ApiModelProperty("排序")
    private Integer sort;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof AssetDictEntity) {
            AssetDictEntity entity = (AssetDictEntity) obj;
            if (entity.name.equals(this.name) && entity.value.equals(this.value) && entity.type.equals(this.type) && entity.pid != null && entity.sort != null && entity.pid.equals(this.pid) && entity.sort.equals(this.sort)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
