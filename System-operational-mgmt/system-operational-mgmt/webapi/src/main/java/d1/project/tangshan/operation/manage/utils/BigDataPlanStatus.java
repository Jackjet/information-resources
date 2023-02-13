package d1.project.tangshan.operation.manage.utils;

/**
 * 数据服务接口类型
 *
 * @author chenghan
 */
public enum BigDataPlanStatus {
    STARTED("started", "已启动"),
    STOPPED("stopped", "已停止");

    private String name;
    private String value;

    BigDataPlanStatus(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }
}
