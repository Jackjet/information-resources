package d1.project.resourcesharingmgmt.resource.model.ReplaceRule;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author zhengyang
 */
@ApiModel(value = "ReplaceRuleUpdateVm", description = "脱敏规则更新")
public class ReplaceRuleUpdateVm extends ReplaceRuleInsertVm{
    @ApiModelProperty("id")
    @NotBlank(message = "id不可为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
