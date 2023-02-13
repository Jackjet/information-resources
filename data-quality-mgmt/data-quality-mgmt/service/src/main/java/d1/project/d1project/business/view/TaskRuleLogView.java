package d1.project.d1project.business.view;

import d1.framework.webapi.annotation.ApiModelProperty;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;

@Entity
@Immutable
@Subselect("SELECT  " +
        "d1_task_rule_log.id as id," +
        "d1_task_rule_log.rule_id as rule_id," +
        "d1_verify_rule.name as rule_name," +
        "d1_verify_rule.metadata_data as metadata_data," +
        "d1_verify_rule.description as rule_description," +
        "d1_task_rule_log.template_id as template_id," +
        "d1_rule_template.name as template_name," +
        "d1_rule_template.description as template_description," +
        "d1_task_rule_log.task_log_id as task_log_id," +
        "d1_task_rule_log.datasource_id as datasource_id," +
        "d1_data_source.name as datasource_name," +
        "d1_task_rule_log.data_size as data_size," +
        "d1_task_rule_log.error_data_size as error_data_size," +
        "d1_task_rule_log.time as time," +
        "d1_task_rule_log.error_data as error_data," +
        "d1_task_rule_log.create_time as create_time " +
        "FROM d1_task_rule_log " +
        "LEFT JOIN d1_verify_rule ON d1_verify_rule.id = d1_task_rule_log.rule_id " +
        "LEFT JOIN d1_rule_template ON d1_rule_template.id = d1_task_rule_log.template_id " +
        "LEFT JOIN d1_data_source ON d1_data_source.id = d1_task_rule_log.datasource_id ")
public class TaskRuleLogView {
    @Id
    private String id;

    @ApiModelProperty("规则id")
    private String ruleId;

    @ApiModelProperty("规则名称")
    private String ruleName;

    @ApiModelProperty("规则描述")
    private String ruleDescription;

    @ApiModelProperty("元数据：json格式表示")
    private String metadataData;

    @ApiModelProperty("规则模板id")
    private String templateId;

    @ApiModelProperty("规则模板名称")
    private String templateName;

    @ApiModelProperty("模板描述")
    private String templateDescription;

    @ApiModelProperty("任务日志id")
    private String taskLogId;

    @ApiModelProperty("数据源id")
    private String datasourceId;

    @ApiModelProperty("数据源名称")
    private String datasourceName;

    @ApiModelProperty("核查数据总数")
    private long dataSize;

    @ApiModelProperty("核查错误数据量")
    private long errorDataSize;

    @ApiModelProperty("核查错误数,JsonArray格式")
    private String errorData;

    @ApiModelProperty("核查错误数据量")
    private int time;

    @ApiModelProperty(
            value = "创建时间",
            example = "2018-01-01 01:01:01"
    )
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleDescription() {
        return ruleDescription;
    }

    public void setRuleDescription(String ruleDescription) {
        this.ruleDescription = ruleDescription;
    }

    public String getMetadataData() {
        return metadataData;
    }

    public void setMetadataData(String metadataData) {
        this.metadataData = metadataData;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateDescription() {
        return templateDescription;
    }

    public void setTemplateDescription(String templateDescription) {
        this.templateDescription = templateDescription;
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

    public String getDatasourceName() {
        return datasourceName;
    }

    public void setDatasourceName(String datasourceName) {
        this.datasourceName = datasourceName;
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

    public String getErrorData() {
        return errorData;
    }

    public void setErrorData(String errorData) {
        this.errorData = errorData;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }
}
