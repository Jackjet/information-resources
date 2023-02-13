package d1.project.tangshan.operation.manage.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author zhangyn
 */
@ApiModel(value = "PageableVm", description = "分页")
public class PageableVm {
    @ApiModelProperty(value = "页数")
    private Integer page;
    @ApiModelProperty(value = "每页条数")
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
