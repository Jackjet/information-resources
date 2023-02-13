package d1.project.resourcesharingmgmt.resource.model.ReplaceRule;

import d1.framework.webapi.annotation.ApiModel;
import d1.project.resourcesharingmgmt.common.model.TimePageableVm;

/**
 * @author zhengyang
 */

@ApiModel(value = "ReplaceRuleFindAllVm", description = "脱敏规则分页查询")
public class ReplaceRuleFindAllVm extends TimePageableVm {
    /**
     * 规则名称
     */
    private String name;

    /**
     * 是否是系统初始化 0 否、1 是
     */
    private String hasSystem;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHasSystem() {
        return hasSystem;
    }

    public void setHasSystem(String hasSystem) {
        this.hasSystem = hasSystem;
    }
}
