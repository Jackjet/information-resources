package d1.project.resourcesharingmgmt.resource.model.Screen;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * 大屏目录统计
 *
 * @author zhengyang
 */
@ApiModel(value = "ArchUpdateCycCountVm", description = "大屏更新周期类别占比")
public class ArchUpdateCycCountVm {
    /**
     * 更新周期名称
     */
    @ApiModelProperty("更新周期名称")
    private String name;

    /**
     * 更新周期百分比
     */
    @ApiModelProperty("更新周期百分比")
    private String num;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
