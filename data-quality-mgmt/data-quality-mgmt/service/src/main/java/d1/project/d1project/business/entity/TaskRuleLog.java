package d1.project.d1project.business.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author libin
 */
@Entity
@Table(name = "d1_task_rule_log")
@ApiModel(value = "TaskRuleLog", description = "规则任务明细表")
public class TaskRuleLog extends BaseCreateEntity {
    @ApiModelProperty("规则id")
    private String ruleId;

    @ApiModelProperty("规则模板id")
    private String templateId;

    @ApiModelProperty("任务日志id")
    private String taskLogId;

    @ApiModelProperty("数据源id")
    private String datasourceId;

    @ApiModelProperty("核查数据总数")
    private long dataSize;

    @ApiModelProperty("核查错误数据量")
    private long errorDataSize;

    @ApiModelProperty("核查错误数,JsonArray格式")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    @Lob
    private String errorData;

    @ApiModelProperty("耗时")
    private long time;

    @ApiModelProperty("创建日期：yyyy-MM-dd")
    private String createDay;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTaskLogId() {
        return taskLogId;
    }

    public void setTaskLogId(String taskLogId) {
        this.taskLogId = taskLogId;
    }

    public String getDatasourceId() {
        return datasourceId;
    }

    public void setDatasourceId(String datasourceId) {
        this.datasourceId = datasourceId;
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

    public String getErrorData() {
        return errorData;
    }

    public void setErrorData(String errorData) {
        this.errorData = errorData;
    }

    public String getCreateDay() {
        return createDay;
    }

    public void setCreateDay(String createDay) {
        this.createDay = createDay;
    }
}
