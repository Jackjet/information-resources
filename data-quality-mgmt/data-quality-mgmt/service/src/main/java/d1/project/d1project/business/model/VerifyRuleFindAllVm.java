package d1.project.d1project.business.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

@ApiModel(value = "VerifyRuleFindAllVm", description = "分页查找检查规则")
public class VerifyRuleFindAllVm extends PageableVm {

    @ApiModelProperty("规则名称")
    private String name;

    @ApiModelProperty("规则状态：0失效、1生效")
    private String status;

    @ApiModelProperty("模板名称")
    private String ruleTemplateName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRuleTemplateName() {
        return ruleTemplateName;
    }

    public void setRuleTemplateName(String ruleTemplateName) {
        this.ruleTemplateName = ruleTemplateName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
