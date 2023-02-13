package d1.project.d1project.business.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

@ApiModel(value = "VerifyRuleLogFindAllVm", description = "分页查找检查规则")
public class VerifyRuleLogFindAllVm extends PageableVm {


    @ApiModelProperty("规则id")
    private String verifyruleid;

    public String getVerifyruleid() {
        return verifyruleid;
    }

    public void setVerifyruleid(String verifyruleid) {
        this.verifyruleid = verifyruleid;
    }
}
