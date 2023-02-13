package d1.project.resource.resourcemanage.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

@ApiModel(value = "DataSourceManageGetDataSourceGetVm", description = "查询数据源")
public class DataSourceManageGetDataSourceGetVm extends PageableVm {
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("类型")
    private String type;
    @ApiModelProperty("分组")
    private String groupId;
    @ApiModelProperty("状态")
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
