package d1.project.d1project.business.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "VerifyRuleUpdateVm", description = "编辑质量检查规则")
public class VerifyRuleUpdateVm {

    @ApiModelProperty("id")
    @NotBlank(message = "规则id不可为空")
    private String id;

    @ApiModelProperty("描述")
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
