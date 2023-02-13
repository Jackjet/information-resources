package d1.project.resourcesharingmgmt.resource.model.ReplaceRule;

import d1.framework.webapi.annotation.ApiModel;
import d1.project.resourcesharingmgmt.common.model.TimePageableVm;

/**
 * @author zhengyang
 */

@ApiModel(value = "ReplaceRuleFindAllListVm", description = "脱敏规则查询")
public class ReplaceRuleFindAllListVm {
    /**
     * 规则名称
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
