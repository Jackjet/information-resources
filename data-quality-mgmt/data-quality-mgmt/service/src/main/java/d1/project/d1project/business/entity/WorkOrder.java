package d1.project.d1project.business.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "d1_work_order")
@ApiModel(value = "WorkOrder", description = "数据质量工单")
public class WorkOrder extends BaseCreateAndUpdateEntity {

    @ApiModelProperty("创建人名称")
    private String createByName;

    @ApiModelProperty("规则id")
    private String verifyRuleId;

    @ApiModelProperty("规则名称")
    private String verifyRuleName;

    @ApiModelProperty("工单标题")
    private String title;

    @ApiModelProperty("级别：0正常、1一般、2关注、3警告、4严重警告")
    private int level;

    @ApiModelProperty("规则描述")
    @Column(columnDefinition = "TEXT")
    private String description;

    @ApiModelProperty("处理人id")
    private String handlerId;

    @ApiModelProperty("处理人名称")
    private String handlerName;

    @ApiModelProperty("处理人时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar handlerTime;

    @ApiModelProperty("状态：0处理中、1已通过、2已驳回")
    private int status;

    @ApiModelProperty("处理意见")
    @Column(columnDefinition = "TEXT")
    private String handlerOpinion;

    @ApiModelProperty("处理周期")
    private String handlerPeriod;
    @Transient
    @ApiModelProperty("数据库名")
    private String dataBasesName;
    @Transient
    @ApiModelProperty("表名")
    private String tableName;
    @Transient
    @ApiModelProperty("数据源信息")
    private String metadataData;

    public String getVerifyRuleId() {
        return verifyRuleId;
    }

    public void setVerifyRuleId(String verifyRuleId) {
        this.verifyRuleId = verifyRuleId;
    }

    public String getVerifyRuleName() {
        return verifyRuleName;
    }

    public void setVerifyRuleName(String verifyRuleName) {
        this.verifyRuleName = verifyRuleName;
    }

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getHandlerOpinion() {
        return handlerOpinion;
    }

    public void setHandlerOpinion(String handlerOpinion) {
        this.handlerOpinion = handlerOpinion;
    }

    public String getHandlerPeriod() {
        return handlerPeriod;
    }

    public void setHandlerPeriod(String handlerPeriod) {
        this.handlerPeriod = handlerPeriod;
    }

    public Calendar getHandlerTime() {
        return handlerTime;
    }

    public void setHandlerTime(Calendar handlerTime) {
        this.handlerTime = handlerTime;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getDataBasesName() {
        return dataBasesName;
    }

    public void setDataBasesName(String dataBasesName) {
        this.dataBasesName = dataBasesName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getMetadataData() {
        return metadataData;
    }

    public void setMetadataData(String metadataData) {
        this.metadataData = metadataData;
    }
}
