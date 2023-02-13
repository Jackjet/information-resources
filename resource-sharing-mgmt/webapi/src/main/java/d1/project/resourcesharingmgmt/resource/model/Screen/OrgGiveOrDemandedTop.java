package d1.project.resourcesharingmgmt.resource.model.Screen;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import java.util.List;

/**
 * 部门使用
 *
 * @author zhengyang
 */
@ApiModel(value = "OrgGiveOrDemandedTop", description = "数据提供部门OR部门需求top10")
public class OrgGiveOrDemandedTop {
    /**
     * 部门提供或需求数
     */
    @ApiModelProperty("部门提供或需求数")
    private long count;

    /**
     * top10
     */
    @ApiModelProperty("top10")
    private List<OrgUseCount> top;

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<OrgUseCount> getTop() {
        return top;
    }

    public void setTop(List<OrgUseCount> top) {
        this.top = top;
    }
}
