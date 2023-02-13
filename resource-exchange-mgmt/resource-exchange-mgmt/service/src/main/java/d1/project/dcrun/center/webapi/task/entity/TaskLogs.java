package d1.project.dcrun.center.webapi.task.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import d1.framework.webapi.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Calendar;

/**
 * @author zhengyang
 */
@Entity
@Table(name = "d1_task_logs")
@ApiModel(value = "TaskLogs", description = "数据集成任务日志")
public class TaskLogs extends BaseEntity {
    @ApiModelProperty("任务id")
    @Column(length = 32)
    private String taskId;

    @ApiModelProperty("执行状态：0成功，1失败")
    @Column(length = 1)
    private int status;

    @ApiModelProperty("调度时间")
    private Calendar runTime;

    @ApiModelProperty("开始时间")
    private Calendar startTime;

    @ApiModelProperty("结速时间")
    private Calendar endTime;

    @ApiModelProperty("总数据量")
    private Long dataSum;

    @ApiModelProperty("输入数据")
    private Long inputNum;

    @ApiModelProperty("输出数据")
    private Long outputNum;

    @ApiModelProperty("写入据量")
    private Long writeNum;

    @ApiModelProperty("读取据量")
    private Long readNum;

    @ApiModelProperty("更新据量")
    private Long updateNum;

    @ApiModelProperty("错误据量")
    private Long errorNum;

    @ApiModelProperty("耗时")
    @Column(length = 20)
    private String sumTime;

    @ApiModelProperty("集成模式：0未知，1定时，2实时")
    @Column(length = 1)
    private int mode;

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

    public Long getDataSum() {
        return dataSum;
    }

    public void setDataSum(Long dataSum) {
        this.dataSum = dataSum;
    }

    public Long getInputNum() {
        return inputNum;
    }

    public void setInputNum(Long inputNum) {
        this.inputNum = inputNum;
    }

    public Long getOutputNum() {
        return outputNum;
    }

    public void setOutputNum(Long outputNum) {
        this.outputNum = outputNum;
    }

    public Long getWriteNum() {
        return writeNum;
    }

    public void setWriteNum(Long writeNum) {
        this.writeNum = writeNum;
    }

    public Long getReadNum() {
        return readNum;
    }

    public void setReadNum(Long readNum) {
        this.readNum = readNum;
    }

    public Long getUpdateNum() {
        return updateNum;
    }

    public void setUpdateNum(Long updateNum) {
        this.updateNum = updateNum;
    }

    public Long getErrorNum() {
        return errorNum;
    }

    public void setErrorNum(Long errorNum) {
        this.errorNum = errorNum;
    }

    public String getSumTime() {
        return sumTime;
    }

    public void setSumTime(String sumTime) {
        this.sumTime = sumTime;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
