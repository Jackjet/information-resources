package d1.project.dcrun.center.webapi.task.model;


import io.swagger.annotations.ApiModelProperty;

public class TaskLogsAnalysisVm {
    @ApiModelProperty("任务总次数")
    private long taskNum;

    @ApiModelProperty("日志总次数")
    private long logNum;

    @ApiModelProperty("成功次数")
    private long successNum;

    @ApiModelProperty("总数据量")
    private long dataSum;

    @ApiModelProperty("成功率")
    private String rateOfSuccess;

    public long getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(long taskNum) {
        this.taskNum = taskNum;
    }

    public long getLogNum() {
        return logNum;
    }

    public void setLogNum(long logNum) {
        this.logNum = logNum;
    }

    public long getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(long successNum) {
        this.successNum = successNum;
    }

    public long getDataSum() {
        return dataSum;
    }

    public void setDataSum(long dataSum) {
        this.dataSum = dataSum;
    }

    public String getRateOfSuccess() {
        return rateOfSuccess;
    }

    public void setRateOfSuccess(String rateOfSuccess) {
        this.rateOfSuccess = rateOfSuccess;
    }

    public TaskLogsAnalysisVm(long logNum, long successNum, long dataSum) {
        this.logNum = logNum;
        this.successNum = successNum;
        this.dataSum = dataSum;
    }

    public TaskLogsAnalysisVm() {
    }
}
