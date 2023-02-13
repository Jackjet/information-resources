package d1.project.dcrun.center.webapi.common.util;

/**
 * 系统服务类型
 *
 * @author chengh
 */
public enum ServiceType {

    /**
     * 节点基础运行服务
     */
    NODE("node", "节点运行服务"),

    /**
     * 脚本运行服务
     */
    WEBAPI("webapi", "脚本运行服务"),

    /**
     * api网关服务
     */
    DCAPI("dcapi", "API网关服务"),

    /**
     * 消息集成服务
     */
    EMQ("emq", "消息集成服务"),

    /**
     * 数据缓存服务
     */
    DATACACHE("datacache", "数据缓存服务");

    /**
     * 英文名
     */
    private String name;
    /**
     * 中文名
     */
    private String chineseName;

    ServiceType(String name, String chineseName) {
        this.name = name;
        this.chineseName = chineseName;
    }

    public static ServiceType getServiceTypeByName(String name) {
        for (ServiceType serviceType : ServiceType.values()) {
            if (serviceType.getName().equals(name.toLowerCase())) {
                return serviceType;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getChineseName() {
        return chineseName;
    }
}
