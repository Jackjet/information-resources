package d1.project.resourcesharingmgmt.resource.model.Screen;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * 大屏目录统计
 *
 * @author zhengyang
 */
@ApiModel(value = "ArchCountVm", description = "大屏目录统计")
public class ArchCountVm {
    /**
     * 入驻部门
     */
    @ApiModelProperty("入驻部门")
    private long org;

    /**
     * 资源目录
     */
    @ApiModelProperty("资源目录")
    private long uview;

    /**
     * 有数据的资源目录
     */
    @ApiModelProperty("有数据的资源目录")
    private long uviewForData;


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

    public long getUviewForData() {
        return uviewForData;
    }

    public void setUviewForData(long uviewForData) {
        this.uviewForData = uviewForData;
    }
}
