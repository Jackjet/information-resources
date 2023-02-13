package d1.project.dataintegration.task.model;

import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * @author zhengyang
 */
public class TaskInfoForContainerVm {
    @ApiModelProperty("任务数")
    private long taskNum;

    @ApiModelProperty("定时任务数")
    private long timingNum;

    @ApiModelProperty("实时任务数")
    private long realTimeNum;

    @ApiModelProperty("启动任务数")
    private long startNum;

    @ApiModelProperty("停止任务数")
    private long endNum;

    public long getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(long taskNum) {
        this.taskNum = taskNum;
    }

    public long getTimingNum() {
        return timingNum;
    }

    public void setTimingNum(long timingNum) {
        this.timingNum = timingNum;
    }

    public long getRealTimeNum() {
        return realTimeNum;
    }

    public void setRealTimeNum(long realTimeNum) {
        this.realTimeNum = realTimeNum;
    }

    public long getStartNum() {
        return startNum;
    }

    public void setStartNum(long startNum) {
        this.startNum = startNum;
    }

    public long getEndNum() {
        return endNum;
    }

    public void setEndNum(long endNum) {
        this.endNum = endNum;
    }

    public TaskInfoForContainerVm(long taskNum, long timingNum, long realTimeNum, long startNum, long endNum) {
        this.taskNum = taskNum;
        this.timingNum = timingNum;
        this.realTimeNum = realTimeNum;
        this.startNum = startNum;
        this.endNum = endNum;
    }

    public TaskInfoForContainerVm(){}
}
