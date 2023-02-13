package d1.project.dcrun.center.webapi.common.util;

/**
 * @author chengh
 */
public enum OperationType {
    /**
     * 系统服务的安装
     */
    INSTALL("install", "安装"),

    /**
     * 系统服务的启动
     */
    START("start", "启动"),

    /**
     * 系统服务的停止
     */
    STOP("stop", "停止"),

    /**
     * 系统服务的删除
     */
    DELETE("delete", "删除"),

    /**
     * web :同步config目录下面配置文件
     * dc-api: 同步application.properties 文件
     * emq: 同步 emq_service_config.json 文件, 配置emq的数据库连接信息
     */
    SYNC("sync", "远程更新配置文件"),

    /**
     * 脚本运行服务同步脚本文件
     */
    SYNC_SCRIPT("sync-script", "远程更新脚本文件"),

    /**
     * api网关/emq 同步开发者数据
     */
    SYNC_USER("sync-user", "同步开发者"),

    /**
     * api网关同步路由配置
     */
    SYNC_ROUTES("sync-routes", "同步路由数据"),

    /**
     * emq同步topic授权数据
     */
    SYNC_TOPIC_PERMISSION("sync-topic-permission", "同步topic授权数据"),

    /**
     * 数据缓存服务同步key
     */
    SYNC_KEY("sync-key", "同步Key");

    /**
     * 英文名
     */
    private String name;
    /**
     * 中文名
     */
    private String chineseName;

    OperationType(String name, String chineseName) {
        this.name = name;
        this.chineseName = chineseName;
    }

    /**
     * 根据属性中文名，获取英文名
     *
     * @param chineseName 中文名
     * @return
     */
    public static String getEnglishName(String chineseName) {
        for (OperationType operation : OperationType.values()) {
            if (operation.getChineseName().equals(chineseName)) {
                return operation.getName();
            }
        }
        return null;
    }

    /**
     * 根据属性英文名，获取中文名
     *
     * @param englishName 英文文名
     * @return
     */
    public static String getChineseName(String englishName) {
        for (OperationType operation : OperationType.values()) {
            if (operation.getName().equals(englishName)) {
                return operation.getChineseName();
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
