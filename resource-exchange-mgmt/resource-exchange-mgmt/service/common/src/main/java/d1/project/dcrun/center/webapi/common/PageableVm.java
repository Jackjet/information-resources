package d1.project.dcrun.center.webapi.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author chengh
 */
@ApiModel(value = "PageableVm", description = "分页")
public class PageableVm {
    @ApiModelProperty(value = "页数")
    private Integer page;
    @ApiModelProperty(value = "大小")
    private Integer size;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
