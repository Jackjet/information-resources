package d1.project.resourcesharingmgmt.resource.model.Screen;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * 大屏目录统计
 *
 * @author zhengyang
 */
@ApiModel(value = "ArchMediaFmtCountVm", description = "大屏数据类型分布")
public class ArchMediaFmtCountVm {
    /**
     * 分类名称
     */
    @ApiModelProperty("分类名称")
    private String name;

    /**
     * 分类数量
     */
    @ApiModelProperty("分类数量")
    private long num;

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
