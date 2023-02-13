package d1.project.tangshan.operation.manage.utils;

/**
 * 预警模块
 *
 * @author chenghan
 */
public enum AlarmModule {
    ENVIRONMENT("environment", "基础环境监控预警"),
    SYSTEM("system", "系统监控预警");

    private String name;
    private String value;

    AlarmModule(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }
}
