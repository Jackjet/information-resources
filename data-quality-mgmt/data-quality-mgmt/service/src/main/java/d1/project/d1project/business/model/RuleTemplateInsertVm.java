package d1.project.d1project.business.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "RuleTemplateInsertVm", description = "添加规则模板")
public class RuleTemplateInsertVm {

    @ApiModelProperty("规则模板名称")
    @NotBlank(message = "规则模板名称不可为空")
    private String name;

    @ApiModelProperty("规则模板描述")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
