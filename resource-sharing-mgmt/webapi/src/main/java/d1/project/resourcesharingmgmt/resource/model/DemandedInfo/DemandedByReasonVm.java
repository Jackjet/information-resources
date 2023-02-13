package d1.project.resourcesharingmgmt.resource.model.DemandedInfo;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * 需求汇总需求缘由
 *
 * @author zhengyang
 */
@ApiModel(value = "DemandedByReasonVm", description = "需求汇总需求缘由")
public class DemandedByReasonVm {
    /**
     * 政策相关
     */
    @ApiModelProperty("政策相关")
    private long related;

    /**
     * 非政策相关
     */
    @ApiModelProperty("非政策相关")
    private long unrelated;

    public long getRelated() {
        return related;
    }

    public void setRelated(long related) {
        this.related = related;
    }

    public long getUnrelated() {
        return unrelated;
    }

    public void setUnrelated(long unrelated) {
        this.unrelated = unrelated;
    }
}
