package d1.project.resourcesharingmgmt.resource.model.Screen;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * 大屏文件下载和API调用次数
 *
 * @author zhengyang
 */
@ApiModel(value = "FileAndApiUseCountVm", description = "大屏文件下载和API调用次数")
public class FileAndApiUseCountByMonthVm {
    /**
     * 几号
     */
    @ApiModelProperty("几号")
    private int day;

    /**
     * 调用量
     */
    @ApiModelProperty("调用量")
    private long useCount;

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public long getUseCount() {
        return useCount;
    }

    public void setUseCount(long useCount) {
        this.useCount = useCount;
    }
}
