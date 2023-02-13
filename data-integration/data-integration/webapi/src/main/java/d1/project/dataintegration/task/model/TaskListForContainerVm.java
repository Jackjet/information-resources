package d1.project.dataintegration.task.model;

import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * @author zhengyang
 */
public class TaskListForContainerVm {
    @ApiModelProperty("定时任务采集数据")
    private String name;

    @ApiModelProperty("运行状态：0未启动，1已启动")
    private int status;

    @ApiModelProperty("集成模式：0未知，1定时，2实时")
    private int mode;

    @ApiModelProperty("运行次数")
    private long runNum;

    @ApiModelProperty("数据采集成功次数")
    private long taskSuccessNum;

    @ApiModelProperty("数据采集失败次数")
    private long taskFailNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public long getRunNum() {
        return runNum;
    }

    public void setRunNum(long runNum) {
        this.runNum = runNum;
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

    public TaskListForContainerVm(String name, int status, int mode, long runNum, long taskSuccessNum, long taskFailNum) {
        this.name = name;
        this.status = status;
        this.mode = mode;
        this.runNum = runNum;
        this.taskSuccessNum = taskSuccessNum;
        this.taskFailNum = taskFailNum;
    }

    public TaskListForContainerVm(){}
}
