package d1.project.resourcesharingmgmt.resource.model.DemandedInfo;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * 需求汇总统计数量
 *
 * @author zhengyang
 */
@ApiModel(value = "DemandedAnalysisVm", description = "需求汇总统计数量")
public class DemandedAnalysisVm {
    /**
     * 提出部门数
     */
    @ApiModelProperty("提出部门数")
    private long org;

    /**
     * 需求数量
     */
    @ApiModelProperty("需求数量")
    private long num;

    /**
     * 已处理
     */
    @ApiModelProperty("已处理")
    private long processed;

    /**
     * 已驳回
     */
    @ApiModelProperty("已驳回")
    private long reject;

    public long getOrg() {
        return org;
    }

    public void setOrg(long org) {
        this.org = org;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public long getProcessed() {
        return processed;
    }

    public void setProcessed(long processed) {
        this.processed = processed;
    }

    public long getReject() {
        return reject;
    }

    public void setReject(long reject) {
        this.reject = reject;
    }
}
