package d1.project.resourcesharingmgmt.resource.model.Screen;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * 支持业务
 *
 * @author zhengyang
 */
@ApiModel(value = "SupportBusinessCountVm", description = "支持业务")
public class SupportBusinessCountVm {
    /**
     * 支持业务
     */
    @ApiModelProperty("支持业务")
    private String name;

    /**
     * 数量
     */
    @ApiModelProperty("数量")
    private long num;

    public SupportBusinessCountVm() {
    }

    public SupportBusinessCountVm(String name, long num) {
        this.name = name;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }
}
