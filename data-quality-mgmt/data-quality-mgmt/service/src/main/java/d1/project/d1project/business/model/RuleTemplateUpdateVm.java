package d1.project.d1project.business.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "RuleTemplateUpdateVm", description = "编辑规则模板")
public class RuleTemplateUpdateVm {

    @ApiModelProperty("id")
    @NotBlank(message = "规则模板id不可为空")
    private String id;

    @ApiModelProperty("模板名称")
    private String name;

    @ApiModelProperty("模板描述")
    private String description;


    @ApiModelProperty("外用次数")
    private int useCount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }
}
