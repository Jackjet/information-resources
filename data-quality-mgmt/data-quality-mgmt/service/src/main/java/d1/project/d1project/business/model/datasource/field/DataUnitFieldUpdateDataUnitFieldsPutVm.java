package d1.project.d1project.business.model.datasource.field;

import d1.framework.webapi.annotation.ApiModel;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "DataUnitFieldUpdateDataUnitFieldsPutVm", description = "更新元数据字段")
public class DataUnitFieldUpdateDataUnitFieldsPutVm {
    @NotBlank(message = "数据源id不能为空")
    private String dataSourceId;
    @NotBlank(message = "元数据id不能为空")
    private String dataUnitId;

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getDataUnitId() {
        return dataUnitId;
    }

    public void setDataUnitId(String dataUnitId) {
        this.dataUnitId = dataUnitId;
    }
}
