package d1.project.d1project.business.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;

@Entity
@Table(name = "d1_task")
@ApiModel(value = "Task", description = "接入任务")
public class Task extends BaseCreateEntity {

    @ApiModelProperty("任务名称")
    private String name;

    @ApiModelProperty("执行周期")
    private String cycle;

    @ApiModelProperty("执行时间")
    private String executionTime;

    @ApiModelProperty("执行周")
    private int executionWeek;

    @ApiModelProperty("任务描述")
    private String description;

    @ApiModelProperty("任务状态 0 未启动，1 已启动")
    private String status;

    @ApiModelProperty("cron表达式")
    private String cronStr;

    @ApiModelProperty("最后一次执行时间")
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar lastTime;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCronStr() {
        return cronStr;
    }

    public void setCronStr(String cronStr) {
        this.cronStr = cronStr;
    }

    public Calendar getLastTime() {
        return lastTime;
    }

    public void setLastTime(Calendar lastTime) {
        this.lastTime = lastTime;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
