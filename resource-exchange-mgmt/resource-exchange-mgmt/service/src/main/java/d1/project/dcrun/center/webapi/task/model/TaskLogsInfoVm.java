package d1.project.dcrun.center.webapi.task.model;


import io.swagger.annotations.ApiModelProperty;

/**
 * @author zhengyang
 */
public class TaskLogsInfoVm {
    @ApiModelProperty("任务名称")
    private String name;

    @ApiModelProperty("任务频率")
    private String taskFrequency;

    @ApiModelProperty("采集次数")
    private long logNum;

    @ApiModelProperty("成功次数")
    private long successNum;

    @ApiModelProperty("失败次数")
    private long failNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaskFrequency() {
        return taskFrequency;
    }

    public void setTaskFrequency(String taskFrequency) {
        this.taskFrequency = taskFrequency;
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

    public long getFailNum() {
        return failNum;
    }

    public void setFailNum(long failNum) {
        this.failNum = failNum;
    }

    public TaskLogsInfoVm(long logNum, long successNum, long failNum) {
        this.logNum = logNum;
        this.successNum = successNum;
        this.failNum = failNum;
    }

    public TaskLogsInfoVm() {
    }
}
