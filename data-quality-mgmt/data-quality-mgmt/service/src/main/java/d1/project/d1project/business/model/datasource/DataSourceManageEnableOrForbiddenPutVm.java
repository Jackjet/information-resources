package d1.project.d1project.business.model.datasource;

import d1.framework.webapi.annotation.ApiModel;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "DataSourceManageEnableOrForbiddenPutVm", description = "启用或禁用数据源")
public class DataSourceManageEnableOrForbiddenPutVm {
    @NotBlank(message = "id不能为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
