package d1.project.dataintegration.task.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author zhengyang
 */
/**
 * @author zhengyang
 */
@Entity
@Table(name = "d1_task_analysis")
@ApiModel(value = "TaskAnalysis", description = "数据集成任务分析")
public class TaskAnalysis extends BaseCreateEntity {
    @ApiModelProperty("定时任务采集数据")
    private long timingTaskNum;

    @ApiModelProperty("定时数据量")
    private long timingTaskDataNum;

    @ApiModelProperty("实时数据量")
    private long realTimeTaskNum;

    @ApiModelProperty("数据采集成功次数")
    private long taskSuccessNum;

    @ApiModelProperty("数据采集失败次数")
    private long taskFailNum;

    @ApiModelProperty("日期（Day/Month）")
    @Column(length = 20)
    private String taskDate;

    @ApiModelProperty("类型；1天，2月")
    @Column(length = 2)
    private int type;

    public long getTimingTaskNum() {
        return timingTaskNum;
    }

    public void setTimingTaskNum(long timingTaskNum) {
        this.timingTaskNum = timingTaskNum;
    }

    public long getTimingTaskDataNum() {
        return timingTaskDataNum;
    }

    public void setTimingTaskDataNum(long timingTaskDataNum) {
        this.timingTaskDataNum = timingTaskDataNum;
    }

    public long getRealTimeTaskNum() {
        return realTimeTaskNum;
    }

    public void setRealTimeTaskNum(long realTimeTaskNum) {
        this.realTimeTaskNum = realTimeTaskNum;
    }

    public long getTaskSuccessNum() {
        return taskSuccessNum;
    }

    public void setTaskSuccessNum(long taskSuccessNum) {
        this.taskSuccessNum = taskSuccessNum;
    }

    public long getTaskFailNum() {
        return taskFailNum;
    }

    public void setTaskFailNum(long taskFailNum) {
        this.taskFailNum = taskFailNum;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
