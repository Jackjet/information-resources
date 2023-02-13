package d1.project.dcrun.center.webapi.resource.model;

/**
 * 数据源添加
 *
 * @author baozh
 */
public class DataSourceUpdateVm extends DataSourceInsertVm {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
