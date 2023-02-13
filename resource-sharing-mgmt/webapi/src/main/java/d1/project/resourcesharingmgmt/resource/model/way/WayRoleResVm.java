package d1.project.resourcesharingmgmt.resource.model.way;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.project.resourcesharingmgmt.resource.entity.WayEntity;

import java.util.List;

/**
 * @author dy
 */
@ApiModel(value = "WayRoleResVm", description = "代办/服务指引查询结果vm")
public class WayRoleResVm {
    @ApiModelProperty("代办列表")
    private List<WayEntity> wayEntities;

    @ApiModelProperty("已选择数据")
    private List<String> choose;

    public List<WayEntity> getWayEntities() {
        return wayEntities;
    }

    public void setWayEntities(List<WayEntity> wayEntities) {
        this.wayEntities = wayEntities;
    }

    public List<String> getChoose() {
        return choose;
    }

    public void setChoose(List<String> choose) {
        this.choose = choose;
    }
}
