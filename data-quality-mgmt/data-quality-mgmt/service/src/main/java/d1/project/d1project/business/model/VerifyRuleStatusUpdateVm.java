package d1.project.d1project.business.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ApiModel(value = "RuleTemplateUpdateVm", description = "编辑质量检查规则的状态")
public class VerifyRuleStatusUpdateVm {

    @ApiModelProperty("id")
    @NotBlank(message = "规则id不可为空")
    private String id;

    @ApiModelProperty("规则状态：启用 1/禁用 0")
    @NotNull(message = "规则状态不可为空")
    @Range(min = 0, max = 1, message = "规则状态")
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
