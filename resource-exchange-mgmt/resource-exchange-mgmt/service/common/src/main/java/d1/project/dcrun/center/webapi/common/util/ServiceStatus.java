package d1.project.dcrun.center.webapi.common.util;

/**
 * 系统服务状态类型
 * @author chenghan
 */
public enum ServiceStatus {
    ONLINE("online", "在线"),
    OFFLINE("offline", "离线"),
    INSTALLING("installing", "安装中");

    private String name;
    private String value;

    ServiceStatus(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }
}
