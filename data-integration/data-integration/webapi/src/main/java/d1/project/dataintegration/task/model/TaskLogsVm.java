package d1.project.dataintegration.task.model;

import d1.framework.webapi.annotation.ApiModelProperty;

import java.util.Calendar;

/**
 * @author zhengyang
 */
public class TaskLogsVm {
    @ApiModelProperty("任务id")
    private String taskId;

    @ApiModelProperty("执行状态：0成功，1失败")
    private int status;

    @ApiModelProperty("调度时间")
    private Calendar runTime;

    @ApiModelProperty("开始时间")
    private Calendar startTime;

    @ApiModelProperty("结速时间")
    private Calendar endTime;

    @ApiModelProperty("总数据量")
    private long dataSum;

    @ApiModelProperty("输入数据")
    private long inputNum;

    @ApiModelProperty("输出数据")
    private long outputNum;

    @ApiModelProperty("写入据量")
    private long writeNum;

    @ApiModelProperty("读取据量")
    private long readNum;

    @ApiModelProperty("更新据量")
    private long updateNum;

    @ApiModelProperty("错误据量")
    private long errorNum;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Calendar getRunTime() {
        return runTime;
    }

    public void setRunTime(Calendar runTime) {
        this.runTime = runTime;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public long getDataSum() {
        return dataSum;
    }

    public void setDataSum(long dataSum) {
        this.dataSum = dataSum;
    }

    public long getInputNum() {
        return inputNum;
    }

    public void setInputNum(long inputNum) {
        this.inputNum = inputNum;
    }

    public long getOutputNum() {
        return outputNum;
    }

    public void setOutputNum(long outputNum) {
        this.outputNum = outputNum;
    }

    public long getWriteNum() {
        return writeNum;
    }

    public void setWriteNum(long writeNum) {
        this.writeNum = writeNum;
    }

    public long getReadNum() {
        return readNum;
    }

    public void setReadNum(long readNum) {
        this.readNum = readNum;
    }

    public long getUpdateNum() {
        return updateNum;
    }

    public void setUpdateNum(long updateNum) {
        this.updateNum = updateNum;
    }

    public long getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(long errorNum) {
        this.errorNum = errorNum;
    }

    public TaskLogsVm(String taskId, int status, Calendar runTime, Calendar startTime, Calendar endTime, long dataSum, long inputNum, long outputNum, long writeNum, long readNum, long updateNum, long errorNum) {
        this.taskId = taskId;
        this.status = status;
        this.runTime = runTime;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dataSum = dataSum;
        this.inputNum = inputNum;
        this.outputNum = outputNum;
        this.writeNum = writeNum;
        this.readNum = readNum;
        this.updateNum = updateNum;
        this.errorNum = errorNum;
    }
}
