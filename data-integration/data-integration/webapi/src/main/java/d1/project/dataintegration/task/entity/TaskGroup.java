package d1.project.dataintegration.task.entity;

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
@Table(name = "d1_task_group")
@ApiModel(value = "TaskGroup", description = "数据集成资源组")
public class TaskGroup extends BaseCreateAndUpdateEntity {
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
