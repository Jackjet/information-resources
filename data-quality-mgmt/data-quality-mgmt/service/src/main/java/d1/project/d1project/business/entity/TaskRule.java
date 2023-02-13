package d1.project.d1project.business.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "d1_task_rule")
@ApiModel(value = "TaskRule", description = "任务规则关联表")
public class TaskRule extends BaseEntity {

    @ApiModelProperty("任务id")
    private String taskId;

    @ApiModelProperty("规则id")
    private String ruleId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }
}
