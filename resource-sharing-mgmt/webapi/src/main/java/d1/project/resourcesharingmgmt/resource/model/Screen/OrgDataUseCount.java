package d1.project.resourcesharingmgmt.resource.model.Screen;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * 部门使用
 *
 * @author zhengyang
 */
@ApiModel(value = "OrgDataUseCount", description = "数据归集部门")
public class OrgDataUseCount {
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

    /**
     * 使用数量
     */
    @ApiModelProperty("api使用数量")
    private long api;

    /**
     * 使用数量
     */
    @ApiModelProperty("数据使用数量")
    private long data;

    /**
     * 使用数量
     */
    @ApiModelProperty("文件使用数量")
    private long file;

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

    public long getApi() {
        return api;
    }

    public void setApi(long api) {
        this.api = api;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public long getFile() {
        return file;
    }

    public void setFile(long file) {
        this.file = file;
    }
}
