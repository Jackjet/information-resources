package d1.project.resourcesharingmgmt.resource.model.DemandedInfo;


import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * @author zhengyang
 */
public class DemandedInfoByProvOrgIdVm {
    /**
     * 申请单位id
     */
    private String createDeptId;

    /**
     * 申请单位名称
     */
    private String createDeptName;

    /**
     * 申请数量
     */
    private long demandedNum;

    /**
     * 未受理数量
     */
    private long untreated;

    /**
     * 已受理数量
     */
    private long processed;

    /**
     * 已驳回数量
     */
    private long reject;

    public String getCreateDeptId() {
        return createDeptId;
    }

    public void setCreateDeptId(String createDeptId) {
        this.createDeptId = createDeptId;
    }

    public String getCreateDeptName() {
        return createDeptName;
    }

    public void setCreateDeptName(String createDeptName) {
        this.createDeptName = createDeptName;
    }

    public long getUntreated() {
        return untreated;
    }

    public void setUntreated(long untreated) {
        this.untreated = untreated;
    }

    public long getDemandedNum() {
        return demandedNum;
    }

    public void setDemandedNum(long demandedNum) {
        this.demandedNum = demandedNum;
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
