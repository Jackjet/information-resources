package d1.project.resource.resourcemanage.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author libin
 */
@Entity
@Table(name = "d1_data_source_group")
@ApiModel(value = "DataSourceGroup", description = "数据源组")
public class DataSourceGroup extends BaseEntity {

    @ApiModelProperty("组名")
    private String name;

    @ApiModelProperty("父节点id")
    private String parentId;

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
}
