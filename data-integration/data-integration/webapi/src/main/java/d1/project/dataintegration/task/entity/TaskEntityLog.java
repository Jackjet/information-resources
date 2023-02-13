package d1.project.dataintegration.task.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Calendar;

/**
 * @author baozh
 */

@Entity
@Table(name = "d1_task_entity_log")
@ApiModel(value = "TaskEntityLog", description = "作业内转换数据表")
public class TaskEntityLog extends BaseEntity {
    @ApiModelProperty("任务ID")
    private String taskId;

    @ApiModelProperty("执行状态：0成功，1失败")
    @Column(length = 1)
    private int status;

    @ApiModelProperty("channelId")
    private String channelId;

    @ApiModelProperty("每次任务组ID")
    private String groupId;

    @ApiModelProperty("输入数量")
    private Long inputNum;

    @ApiModelProperty("输出数量")
    private Long outputNum;

    @ApiModelProperty("读取数量")
    private Long readNum;

    @ApiModelProperty("写入数量")
    private Long writeNum;

    @ApiModelProperty("更新数量")
    private Long updateNum;

    @ApiModelProperty("错误数量")
    private Long errorNum;

    @ApiModelProperty("步骤名称")
    private String stepName;

    @ApiModelProperty("创建时间")
    private Calendar createTime;

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

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
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

    public Long getReadNum() {
        return readNum;
    }

    public void setReadNum(Long readNum) {
        this.readNum = readNum;
    }

    public Long getWriteNum() {
        return writeNum;
    }

    public void setWriteNum(Long writeNum) {
        this.writeNum = writeNum;
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

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }
}
