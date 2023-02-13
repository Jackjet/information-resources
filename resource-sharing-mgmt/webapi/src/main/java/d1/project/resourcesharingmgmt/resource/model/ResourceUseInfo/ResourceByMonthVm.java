package d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * 资源使用汇总半年申请量
 *
 * @author zhengyang
 */
@ApiModel(value = "ResourceByMonthVm", description = "资源使用汇总半年申请量")
public class ResourceByMonthVm {
    /**
     * 月份
     */
    @ApiModelProperty("月份")
    private String month;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private long num;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }
}
