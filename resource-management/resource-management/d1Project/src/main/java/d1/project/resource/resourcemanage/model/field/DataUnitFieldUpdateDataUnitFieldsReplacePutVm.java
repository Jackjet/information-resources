package d1.project.resource.resourcemanage.model.field;

import d1.framework.webapi.annotation.ApiModel;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "DataUnitFieldUpdateDataUnitFieldsReplacePutVm", description = "更新元数据脱敏字段")
public class DataUnitFieldUpdateDataUnitFieldsReplacePutVm {
    @NotBlank(message = "元数据字段ID")
    private String id;

    @NotBlank(message = "脱敏规则名称不能为空")
    private String ruleName;

    private Integer hasReplace;

    @NotBlank(message = "脱敏SQL不能为空")
    private String replaceSql;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Integer getHasReplace() {
        return hasReplace;
    }

    public void setHasReplace(Integer hasReplace) {
        this.hasReplace = hasReplace;
    }

    public String getReplaceSql() {
        return replaceSql;
    }

    public void setReplaceSql(String replaceSql) {
        this.replaceSql = replaceSql;
    }
}
