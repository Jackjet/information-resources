package d1.project.resourcesharingmgmt.resource.model.Screen;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * 部门积极度
 *
 * @author zhengyang
 */
@ApiModel(value = "OrgApplyCount", description = "部门积极度")
public class OrgApplyCount {
    /**
     * 使用部门
     */
    @ApiModelProperty("使用部门")
    private String orgName;

    /**
     * 申请总数
     */
    @ApiModelProperty("申请总数")
    private long count;

    /**
     * 0未审核
     */
    @ApiModelProperty("0未审核")
    private long init;

    /**
     * 1初审通过
     */
    @ApiModelProperty("1初审通过")
    private long first;

    /**
     * 2已审核
     */
    @ApiModelProperty("2已审核")
    private long approved;

    /**
     * 3已驳回
     */
    @ApiModelProperty("3已驳回")
    private long reject;

    /**
     * 4审核失败
     */
    @ApiModelProperty("4审核失败")
    private long fail;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getInit() {
        return init;
    }

    public void setInit(long init) {
        this.init = init;
    }

    public long getFirst() {
        return first;
    }

    public void setFirst(long first) {
        this.first = first;
    }

    public long getApproved() {
        return approved;
    }

    public void setApproved(long approved) {
        this.approved = approved;
    }

    public long getReject() {
        return reject;
    }

    public void setReject(long reject) {
        this.reject = reject;
    }

    public long getFail() {
        return fail;
    }

    public void setFail(long fail) {
        this.fail = fail;
    }
}
