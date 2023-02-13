package d1.project.tangshan.operation.manage.utils;

/**
 * 预警模块
 *
 * @author chenghan
 */
public enum DeploymentType {
    INSTALL("install", "自动安装"),
    UPGRADE("upgrade", "版本升级"),
    ROLLBACK("rollback", "版本回滚"),
    CONFIGUPDATE("configUpdate", "配置更新");

    private String name;
    private String value;

    DeploymentType(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }
}
