package d1.project.container.api.model;

/**
 * 分页model，所有搜索返回数据的基类
 *
 * @author Buter
 * @date 2020/3/15 10:30
 */
public class PageableVm {
    /**
     * 第几页(缺省1)
     */
    private Integer page;

    /**
     * 每页大小(缺省10)
     */
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
