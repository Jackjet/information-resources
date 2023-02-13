package d1.project.tangshan.operation.manage.utils;

/**
 * 预警模块
 *
 * @author chenghan
 */
public enum ModuleMonitorType {
    PERFORMANCE("performance", "性能监控"),
    DATA("data", "数据监控"),
    RESOURCES("resources", "资源监控");

    private String name;
    private String value;

    ModuleMonitorType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public static ModuleMonitorType getByName(String name) {
        for (ModuleMonitorType type : ModuleMonitorType.values()) {
            if (name.equals(type.getName())) {
                return type;
            }
        }
        return null;
    }
}
