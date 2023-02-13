package d1.project.d1project.business.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "WorkOrderInsertVm", description = "添加工单")
public class WorkOrderInsertVm {

    @ApiModelProperty("规则id")
    @NotBlank(message = "规则不可为空")
    private String verifyRuleId;

    @ApiModelProperty("规则名称")
    private String verifyRuleName;

    @ApiModelProperty("工单标题")
    @NotBlank(message = "工单标题不可为空")
    private String title;

    @ApiModelProperty("级别：0正常、1一般、2关注、3警告、4严重警告")
    @NotNull(message = "工单级别不可为空")
    @Range(min = 0, max = 4, message = "工单级别")
    private int level;

    @ApiModelProperty("规则描述")
    private String description;

    @ApiModelProperty("处理人id")
    @NotBlank(message = "处理人不可为空")
    private String handlerId;

    @ApiModelProperty("处理人id")
    @NotBlank(message = "处理人名称")
    private String handlerName;

    public String getVerifyRuleId() {
        return verifyRuleId;
    }

    public void setVerifyRuleId(String verifyRuleId) {
        this.verifyRuleId = verifyRuleId;
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

    public String getHandlerId() {
        return handlerId;
    }

    public void setHandlerId(String handlerId) {
        this.handlerId = handlerId;
    }

    public String getVerifyRuleName() {
        return verifyRuleName;
    }

    public void setVerifyRuleName(String verifyRuleName) {
        this.verifyRuleName = verifyRuleName;
    }

    public String getHandlerName() {
        return handlerName;
    }

    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }
}
