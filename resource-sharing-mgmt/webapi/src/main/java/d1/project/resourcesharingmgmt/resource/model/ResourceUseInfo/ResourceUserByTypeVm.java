package d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * 资源汇总资源类型
 *
 * @author zhengyang
 */
@ApiModel(value = "ResourceUserByTypeVm", description = "资源汇总资源类型")
public class ResourceUserByTypeVm {
    /**
     * 云接口
     */
    @ApiModelProperty("云接口")
    private long api;

    /**
     * 云文件
     */
    @ApiModelProperty("云文件")
    private long file;

    /**
     * 云数据
     */
    @ApiModelProperty("云数据")
    private long data;

    public long getApi() {
        return api;
    }

    public void setApi(long api) {
        this.api = api;
    }

    public long getFile() {
        return file;
    }

    public void setFile(long file) {
        this.file = file;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }
}
