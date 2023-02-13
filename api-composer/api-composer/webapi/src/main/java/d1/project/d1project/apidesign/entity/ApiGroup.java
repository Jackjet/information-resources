package d1.project.d1project.apidesign.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author zhengyang
 */
@Entity
@Table(name = "d1_api_group")
@ApiModel(value = "ApiGroup", description = "Api资源组")
public class ApiGroup extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("组名称")
    @Column(length = 100)
    private String name;

    @ApiModelProperty("父级id")
    @Column(length = 32)
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
