package d1.project.d1project.common.model;

import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

public class CommonVerifyRuleFindAllGetVm extends PageableVm {
    @ApiModelProperty("规则名称")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
