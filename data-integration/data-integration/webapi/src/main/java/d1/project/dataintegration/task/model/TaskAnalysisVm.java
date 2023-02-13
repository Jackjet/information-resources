package d1.project.dataintegration.task.model;

import d1.framework.webapi.annotation.ApiModelProperty;
/**
 * @author zhengyang
 */
public class TaskAnalysisVm {
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

    public TaskAnalysisVm(long timingTaskNum, long timingTaskDataNum, long realTimeTaskNum, long taskSuccessNum, long taskFailNum) {
        this.timingTaskNum = timingTaskNum;
        this.timingTaskDataNum = timingTaskDataNum;
        this.realTimeTaskNum = realTimeTaskNum;
        this.taskSuccessNum = taskSuccessNum;
        this.taskFailNum = taskFailNum;
    }

    public TaskAnalysisVm() {
    }
}
