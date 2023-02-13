package d1.project.d1project.business.model.datasource.field;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.model.PageableVm;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "DataUnitFieldGetDataUnitFieldsGetVm", description = "查询元数据字段")
public class DataUnitFieldGetDataUnitFieldsGetVm extends PageableVm {
    @NotBlank(message = "元数据id不能为空")
    private String dataUnitId;

    public String getDataUnitId() {
        return dataUnitId;
    }

    public void setDataUnitId(String dataUnitId) {
        this.dataUnitId = dataUnitId;
    }
}
