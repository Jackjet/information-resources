package d1.project.d1project.business.model.datasource.unit;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.model.PageableVm;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "DataUnitGetDataUnitGetVm", description = "查询元数据")
public class DataUnitGetDataUnitGetVm extends PageableVm {
    @NotBlank(message = "数据源id不能为空")
    private String dataSourceId;

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }
}
