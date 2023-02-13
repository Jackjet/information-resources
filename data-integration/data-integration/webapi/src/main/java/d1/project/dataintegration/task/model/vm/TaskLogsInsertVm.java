package d1.project.dataintegration.task.model.vm;

import d1.framework.webapi.annotation.ApiModelProperty;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import java.util.Calendar;

/**
 * @author zhengyang
 */
public class TaskLogsInsertVm {
    @ApiModelProperty("任务id")
    @NotBlank(message = "请输入任务id")
    private String taskId;

    @ApiModelProperty("执行状态：0成功，1失败")
    @NotBlank(message = "请输入执行状态")
    private int status;

    @ApiModelProperty("调度时间")
    @NotBlank(message = "请输入调度时间")
    private Calendar runTime;

    @ApiModelProperty("开始时间")
    @NotBlank(message = "请输入开始时间")
    private Calendar startTime;

    @ApiModelProperty("结速时间")
    @NotBlank(message = "请输入结速时间")
    private Calendar endTime;

    @ApiModelProperty("总数据量")
    @NotBlank(message = "请输入总数据量")
    private int dataSum;

    @ApiModelProperty("输入数据")
    @NotBlank(message = "请输入数据量")
    private int inputNum;

    @ApiModelProperty("输出数据")
    @NotBlank(message = "请输入输出数据")
    private int outputNum;

    @ApiModelProperty("写入据量")
    @NotBlank(message = "请输入写入据量")
    private int writeNum;

    @ApiModelProperty("读取据量")
    @NotBlank(message = "请输入读取据量")
    private int readNum;

    @ApiModelProperty("更新据量")
    @NotBlank(message = "请输入更新据量")
    private int updateNum;

    @ApiModelProperty("错误据量")
    @NotBlank(message = "请输入错误据量")
    private int errorNum;

    @ApiModelProperty("耗时")
    @NotBlank(message = "请输入耗时")
    @Column(length = 20)
    private String sumTime;

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

    public int getDataSum() {
        return dataSum;
    }

    public void setDataSum(int dataSum) {
        this.dataSum = dataSum;
    }

    public int getInputNum() {
        return inputNum;
    }

    public void setInputNum(int inputNum) {
        this.inputNum = inputNum;
    }

    public int getOutputNum() {
        return outputNum;
    }

    public void setOutputNum(int outputNum) {
        this.outputNum = outputNum;
    }

    public int getWriteNum() {
        return writeNum;
    }

    public void setWriteNum(int writeNum) {
        this.writeNum = writeNum;
    }

    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }

    public int getUpdateNum() {
        return updateNum;
    }

    public void setUpdateNum(int updateNum) {
        this.updateNum = updateNum;
    }

    public int getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(int errorNum) {
        this.errorNum = errorNum;
    }

    public String getSumTime() {
        return sumTime;
    }

    public void setSumTime(String sumTime) {
        this.sumTime = sumTime;
    }
}
