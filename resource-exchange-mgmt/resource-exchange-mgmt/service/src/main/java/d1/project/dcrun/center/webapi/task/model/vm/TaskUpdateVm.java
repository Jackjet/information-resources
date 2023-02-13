package d1.project.dcrun.center.webapi.task.model.vm;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author zhengyang
 */
@ApiModel(value = "TaskUpdateVm", description = "任务修改")
public class TaskUpdateVm {
    @ApiModelProperty("id")
    @NotBlank(message = "id不可为空")
    private String id;

    @ApiModelProperty("任务名称")
    @NotBlank(message = "任务名称不可为空")
    private String name;

    @ApiModelProperty("所属资源组")
    @NotBlank(message = "所属资源组不可为空")
    private String groupId;

    @ApiModelProperty("容器")
    @NotBlank(message = "容器不可为空")
    private String container;

    @ApiModelProperty("起始数据源")
    @NotBlank(message = "起始数据源不可为空")
    private String startDataSource;

    @ApiModelProperty("目标数据源")
    @NotBlank(message = "目标数据源不可为空")
    private String endDataSource;

    @ApiModelProperty("描述")
    private String remark;

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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getStartDataSource() {
        return startDataSource;
    }

    public void setStartDataSource(String startDataSource) {
        this.startDataSource = startDataSource;
    }

    public String getEndDataSource() {
        return endDataSource;
    }

    public void setEndDataSource(String endDataSource) {
        this.endDataSource = endDataSource;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
