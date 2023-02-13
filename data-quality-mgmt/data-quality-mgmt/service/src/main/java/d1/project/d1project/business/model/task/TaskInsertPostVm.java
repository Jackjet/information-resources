package d1.project.d1project.business.model.task;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(value = "TaskInsertPostVm", description = "添加任务")
public class TaskInsertPostVm {
    @NotBlank(message = "任务名称不能为空")
    @ApiModelProperty("任务名称")
    private String name;
    @NotBlank(message = "执行周期不能为空")
    @ApiModelProperty("执行周期")
    private String cycle;
    @NotBlank(message = "执行时间不能为空")
    @ApiModelProperty("执行时间")
    private String executionTime;
    @ApiModelProperty("执行周")
    private int executionWeek;
    @ApiModelProperty("任务描述")
    private String description;
    @NotNull(message = "质量检测规则id不能为空")
    private List<String> ruleIds;
    @ApiModelProperty("组id")
    private String groupId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(String executionTime) {
        this.executionTime = executionTime;
    }

    public int getExecutionWeek() {
        return executionWeek;
    }

    public void setExecutionWeek(int executionWeek) {
        this.executionWeek = executionWeek;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getRuleIds() {
        return ruleIds;
    }

    public void setRuleIds(List<String> ruleIds) {
        this.ruleIds = ruleIds;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
