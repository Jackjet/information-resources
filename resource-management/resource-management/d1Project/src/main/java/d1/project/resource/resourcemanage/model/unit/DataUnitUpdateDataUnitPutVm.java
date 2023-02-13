package d1.project.resource.resourcemanage.model.unit;

import d1.framework.webapi.annotation.ApiModel;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "DataUnitUpdateDataUnitPutVm", description = "更新元数据")
public class DataUnitUpdateDataUnitPutVm {
    @NotBlank(message = "数据源id不能为空")
    private String dataSourceId;

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }
}
