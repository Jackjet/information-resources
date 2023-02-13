package d1.project.resourcesharingmgmt.resource.model.Screen;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * 数据推送和文件下载和API调用次数
 *
 * @author zhengyang
 */
@ApiModel(value = "FileAndApiUseCountVm", description = "数据推送和文件下载和API调用次数")
public class FileAndApiUseCountVm {
    /**
     * 数据推送条数
     */
    @ApiModelProperty("数据推送条数")
    private long dataPushCount;

    /**
     * 文件下载次数
     */
    @ApiModelProperty("文件下载次数")
    private long fileDownCount;

    /**
     * Api调用次数
     */
    @ApiModelProperty("Api调用次数")
    private long apiUseCount;

    public long getDataPushCount() {
        return dataPushCount;
    }

    public void setDataPushCount(long dataPushCount) {
        this.dataPushCount = dataPushCount;
    }

    public long getFileDownCount() {
        return fileDownCount;
    }

    public void setFileDownCount(long fileDownCount) {
        this.fileDownCount = fileDownCount;
    }

    public long getApiUseCount() {
        return apiUseCount;
    }

    public void setApiUseCount(long apiUseCount) {
        this.apiUseCount = apiUseCount;
    }
}
