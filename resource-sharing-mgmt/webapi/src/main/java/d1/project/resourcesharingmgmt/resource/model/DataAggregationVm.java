package d1.project.resourcesharingmgmt.resource.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import java.util.Calendar;

/**
 * 数据汇聚
 *
 * @author zhengyang
 */
@ApiModel(value = "DataAggregationVm", description = "数据汇聚")
public class DataAggregationVm {
    @ApiModelProperty("入驻部门")
    private long org;

    @ApiModelProperty("资源目录")
    private long uview;

    @ApiModelProperty("共享需求")
    private long demanded;

    @ApiModelProperty("资源需求")
    private long resourceUse;

    public long getOrg() {
        return org;
    }

    public void setOrg(long org) {
        this.org = org;
    }

    public long getUview() {
        return uview;
    }

    public void setUview(long uview) {
        this.uview = uview;
    }

    public long getDemanded() {
        return demanded;
    }

    public void setDemanded(long demanded) {
        this.demanded = demanded;
    }

    public long getResourceUse() {
        return resourceUse;
    }

    public void setResourceUse(long resourceUse) {
        this.resourceUse = resourceUse;
    }
}
