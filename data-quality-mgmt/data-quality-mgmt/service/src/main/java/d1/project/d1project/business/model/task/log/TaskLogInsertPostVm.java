package d1.project.d1project.business.model.task.log;

import d1.framework.webapi.annotation.ApiModelProperty;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Calendar;

public class TaskLogInsertPostVm {
    @NotNull(message = "执行时间不能为空")
    @ApiModelProperty("执行时间")
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar executionTime;
    @NotBlank(message = "执行结果不能为空")
    @ApiModelProperty("执行结果")
    private String executionResult;
    @NotNull(message = "检查数据量不能为空")
    @ApiModelProperty("检查数据量")
    private long dataSize;
    @NotNull(message = "问题数据量不能为空")
    @ApiModelProperty("问题数据")
    private long errorDataSize;
    @NotNull(message = "耗时不能为空")
    @ApiModelProperty("耗时")
    private int time;
    @NotBlank(message = "任务id不能为空")
    @ApiModelProperty("任务Id")
    private String taskId;

    public Calendar getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Calendar executionTime) {
        this.executionTime = executionTime;
    }

    public String getExecutionResult() {
        return executionResult;
    }

    public void setExecutionResult(String executionResult) {
        this.executionResult = executionResult;
    }

    public long getDataSize() {
        return dataSize;
    }

    public void setDataSize(long dataSize) {
        this.dataSize = dataSize;
    }

    public long getErrorDataSize() {
        return errorDataSize;
    }

    public void setErrorDataSize(long errorDataSize) {
        this.errorDataSize = errorDataSize;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
