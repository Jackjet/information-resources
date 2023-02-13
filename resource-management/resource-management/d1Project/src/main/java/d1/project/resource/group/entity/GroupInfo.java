package d1.project.resource.group.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author baozh
 */

@Entity
@Table(name = "d1_group_info")
@ApiModel(value = "GroupInfo", description = "分组管理")
public class GroupInfo extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("名称")
    @Column(length = 50)
    private String name;

    @ApiModelProperty("类型(0:API,1:容器,2:数据源,3:标签)")
    @Column(length = 2)
    private String type;

    @ApiModelProperty("父级ID")
    @Column(length = 50)
    private String parentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
