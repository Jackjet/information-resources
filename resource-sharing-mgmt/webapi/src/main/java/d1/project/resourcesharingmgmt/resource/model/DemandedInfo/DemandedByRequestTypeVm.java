package d1.project.resourcesharingmgmt.resource.model.DemandedInfo;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * 需求汇总需求类型
 *
 * @author zhengyang
 */
@ApiModel(value = "DemandedByRequestTypeVm", description = "需求汇总需求类型")
public class DemandedByRequestTypeVm {
    /**
     * 目录资源变更
     */
    @ApiModelProperty("目录资源变更")
    private long archUpdate;

    /**
     * 目录资源新增
     */
    @ApiModelProperty("目录资源新增")
    private long archAdd;

    /**
     * 云数据变更
     */
    @ApiModelProperty("云数据变更")
    private long dataUpdate;

    /**
     * 云数据新增
     */
    @ApiModelProperty("云数据新增")
    private long dataAdd;

    public long getArchUpdate() {
        return archUpdate;
    }

    public void setArchUpdate(long archUpdate) {
        this.archUpdate = archUpdate;
    }

    public long getArchAdd() {
        return archAdd;
    }

    public void setArchAdd(long archAdd) {
        this.archAdd = archAdd;
    }

    public long getDataUpdate() {
        return dataUpdate;
    }

    public void setDataUpdate(long dataUpdate) {
        this.dataUpdate = dataUpdate;
    }

    public long getDataAdd() {
        return dataAdd;
    }

    public void setDataAdd(long dataAdd) {
        this.dataAdd = dataAdd;
    }
}
