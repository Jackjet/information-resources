package d1.project.resourcesharingmgmt.resource.model.Screen;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * 部门使用
 *
 * @author zhengyang
 */
@ApiModel(value = "OrgArchAndUseCount", description = "资源目录数与数据提供量提供最完整的TOP10部门")
public class OrgArchAndUseCount {
    /**
     * 使用部门
     */
    @ApiModelProperty("使用部门")
    private String orgName;

    /**
     * 资源目录数量
     */
    @ApiModelProperty("资源目录数量")
    private long count;

    /**
     * 提供数量
     */
    @ApiModelProperty("提供数量")
    private long data;

    /**
     * 比例
     */
    @ApiModelProperty("比例")
    private float percent;

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

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }
}
