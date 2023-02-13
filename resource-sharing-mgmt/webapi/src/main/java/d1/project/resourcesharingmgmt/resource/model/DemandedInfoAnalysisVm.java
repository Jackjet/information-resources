package d1.project.resourcesharingmgmt.resource.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * 我的待办-需求申请
 *
 * @author zhengyang
 */
@ApiModel(value = "DemandedInfoAnalysisVm", description = "我的待办-需求申请")
public class DemandedInfoAnalysisVm {
    @ApiModelProperty("目录资源新增")
    private long archBusiInsert;

    @ApiModelProperty("目录资源新增待审批")
    private long archBusiInsertNotApproval;

    @ApiModelProperty("目录资源变更")
    private long archBusiUpdate;

    @ApiModelProperty("目录资源变更待审批")
    private long archBusiUpdateNotApproval;

    @ApiModelProperty("云数据新增")
    private long apiInsert;

    @ApiModelProperty("云数据新增待审批")
    private long apiInsertNotApproval;

    @ApiModelProperty("云数据变更")
    private long apiUpdate;

    @ApiModelProperty("云数据变更待审批")
    private long apiUpdateNotApproval;

    public long getArchBusiInsert() {
        return archBusiInsert;
    }

    public void setArchBusiInsert(long archBusiInsert) {
        this.archBusiInsert = archBusiInsert;
    }

    public long getArchBusiInsertNotApproval() {
        return archBusiInsertNotApproval;
    }

    public void setArchBusiInsertNotApproval(long archBusiInsertNotApproval) {
        this.archBusiInsertNotApproval = archBusiInsertNotApproval;
    }

    public long getArchBusiUpdate() {
        return archBusiUpdate;
    }

    public void setArchBusiUpdate(long archBusiUpdate) {
        this.archBusiUpdate = archBusiUpdate;
    }

    public long getArchBusiUpdateNotApproval() {
        return archBusiUpdateNotApproval;
    }

    public void setArchBusiUpdateNotApproval(long archBusiUpdateNotApproval) {
        this.archBusiUpdateNotApproval = archBusiUpdateNotApproval;
    }

    public long getApiInsert() {
        return apiInsert;
    }

    public void setApiInsert(long apiInsert) {
        this.apiInsert = apiInsert;
    }

    public long getApiInsertNotApproval() {
        return apiInsertNotApproval;
    }

    public void setApiInsertNotApproval(long apiInsertNotApproval) {
        this.apiInsertNotApproval = apiInsertNotApproval;
    }

    public long getApiUpdate() {
        return apiUpdate;
    }

    public void setApiUpdate(long apiUpdate) {
        this.apiUpdate = apiUpdate;
    }

    public long getApiUpdateNotApproval() {
        return apiUpdateNotApproval;
    }

    public void setApiUpdateNotApproval(long apiUpdateNotApproval) {
        this.apiUpdateNotApproval = apiUpdateNotApproval;
    }

    public DemandedInfoAnalysisVm() {
    }

    public DemandedInfoAnalysisVm(long archBusiInsert, long archBusiInsertNotApproval, long archBusiUpdate, long archBusiUpdateNotApproval, long apiInsert, long apiInsertNotApproval, long apiUpdate, long apiUpdateNotApproval) {
        this.archBusiInsert = archBusiInsert;
        this.archBusiInsertNotApproval = archBusiInsertNotApproval;
        this.archBusiUpdate = archBusiUpdate;
        this.archBusiUpdateNotApproval = archBusiUpdateNotApproval;
        this.apiInsert = apiInsert;
        this.apiInsertNotApproval = apiInsertNotApproval;
        this.apiUpdate = apiUpdate;
        this.apiUpdateNotApproval = apiUpdateNotApproval;
    }
}
