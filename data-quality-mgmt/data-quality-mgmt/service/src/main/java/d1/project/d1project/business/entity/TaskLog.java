package d1.project.d1project.business.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseEntity;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;

@Entity
@Table(name = "d1_task_log")
@ApiModel(value = "TaskLog", description = "任务日志")
public class TaskLog extends BaseEntity {

    @ApiModelProperty("执行时间")
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar executionTime;

    @ApiModelProperty("执行结果，0 失败 1 成功")
    private String executionResult;

    @ApiModelProperty("检查数据量")
    private long dataSize;

    @ApiModelProperty("问题数据")
    private long errorDataSize;

    @ApiModelProperty("耗时")
    private long time;

    @ApiModelProperty("任务Id")
    private String taskId;

    @ApiModelProperty("执行年月")
    private String yearMonth;

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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }
}
