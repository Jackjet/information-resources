package d1.project.d1project.business.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

@ApiModel(value = "RuleTemplateFindAllVm", description = "分页查找规则模板")
public class RuleTemplateFindAllVm extends PageableVm {

    @ApiModelProperty("模板名称")
    private String name;

    @ApiModelProperty("模板状态：0禁用、1启用")
    private String status;

    @ApiModelProperty("模板类型：0内置、1自定义")
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
