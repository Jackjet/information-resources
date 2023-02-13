package d1.project.resourcesharingmgmt.resource.model.Screen;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * 部门使用
 *
 * @author zhengyang
 */
@ApiModel(value = "OrgUseCount", description = "部门使用")
public class OrgUseCount {
    /**
     * 使用部门
     */
    @ApiModelProperty("使用部门")
    private String orgName;

    /**
     * 使用数量
     */
    @ApiModelProperty("使用数量")
    private long count;

    public OrgUseCount() {
    }

    public OrgUseCount(String orgName, long count) {
        this.orgName = orgName;
        this.count = count;
    }

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
}
