package d1.project.d1project.business.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "VerifyRuleInsertVm", description = "添加检查规则")
public class VerifyRuleInsertVm {

    @ApiModelProperty("规则名称")
    @NotBlank(message = "规则名称不可为空")
    private String name;

    @ApiModelProperty("模板id")
    @NotBlank(message = "模板id不可为空")
    private String ruleTemplateId;

    @ApiModelProperty("规则描述")
    private String description;

    @ApiModelProperty("元数据：json格式表示")
    @NotBlank(message = "元数据不可为空")
    private String metadataData;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRuleTemplateId() {
        return ruleTemplateId;
    }

    public void setRuleTemplateId(String ruleTemplateId) {
        this.ruleTemplateId = ruleTemplateId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMetadataData() {
        return metadataData;
    }

    public void setMetadataData(String metadataData) {
        this.metadataData = metadataData;
    }
}
