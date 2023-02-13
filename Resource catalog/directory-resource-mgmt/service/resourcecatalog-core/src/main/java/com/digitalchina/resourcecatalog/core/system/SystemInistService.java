//package com.digitalchina.resourcecatalog.core.system;
//
//import com.digitalchina.resourcecatalog.core.util.SystemInfoPrinter;
//import com.digitalchina.resourcecatalog.db.service.ResourcecatalogSystemConfigService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.Map;
//
///**
// * 系统启动服务，用于设置系统配置信息、检查系统状态及打印系统信息
// */
//@Component
//class SystemInistService {
//    private SystemInistService systemInistService;
//
//
//    @Autowired
//    private Environment environment;
//
//    @PostConstruct
//    private void inist() {
//        systemInistService = this;
//        initConfigs();
//        SystemInfoPrinter.printInfo("Resourcecatalog 初始化信息", getSystemInfo());
//    }
//
//
//    private final static Map<String, String> DEFAULT_CONFIGS = new HashMap<>();
//
//    static {
//
//    }
//
//    @Autowired
//    private ResourcecatalogSystemConfigService resourcecatalogSystemConfigService;
//
//    private void initConfigs() {
//        // 1. 读取数据库全部配置信息
//        Map<String, String> configs =resourcecatalogSystemConfigService.queryAll();
//
//        // 2. 分析DEFAULT_CONFIGS
//        for (Map.Entry<String, String> entry : DEFAULT_CONFIGS.entrySet()) {
//            if (configs.containsKey(entry.getKey())) {
//                continue;
//            }
//
//            configs.put(entry.getKey(), entry.getValue());
//           resourcecatalogSystemConfigService.addConfig(entry.getKey(), entry.getValue());
//        }
//
//    }
//
//    private Map<String, String> getSystemInfo() {
//
//        Map<String, String> infos = new LinkedHashMap<>();
//
//        infos.put(SystemInfoPrinter.CREATE_PART_COPPER + 0, "系统信息");
//        // 测试获取application-db.yml配置信息
//        infos.put("服务器端口", environment.getProperty("server.port"));
//        infos.put("数据库USER", environment.getProperty("spring.datasource.druid.username"));
//        infos.put("数据库地址", environment.getProperty("spring.datasource.druid.url"));
//        infos.put("调试级别", environment.getProperty("logging.level.com.digitalchina.resourcecatalog.wx"));
//
//        // 测试获取application-core.yml配置信息
//        infos.put(SystemInfoPrinter.CREATE_PART_COPPER + 1, "模块状态");
//        infos.put("邮件", environment.getProperty("resourcecatalog.notify.mail.enable"));
//        infos.put("短信", environment.getProperty("resourcecatalog.notify.sms.enable"));
//        infos.put("模版消息", environment.getProperty("resourcecatalog.notify.wx.enable"));
//        infos.put("快递信息", environment.getProperty("resourcecatalog.express.enable"));
//        infos.put("快递鸟ID", environment.getProperty("resourcecatalog.express.appId"));
//        infos.put("对象存储", environment.getProperty("resourcecatalog.storage.active"));
//        infos.put("本地对象存储路径", environment.getProperty("resourcecatalog.storage.local.storagePath"));
//        infos.put("本地对象访问地址", environment.getProperty("resourcecatalog.storage.local.address"));
//        infos.put("本地对象访问端口", environment.getProperty("resourcecatalog.storage.local.port"));
//
//        // 微信相关信息
//        infos.put(SystemInfoPrinter.CREATE_PART_COPPER + 2, "微信相关");
//        infos.put("微信APP KEY", environment.getProperty("resourcecatalog.wx.app-id"));
//        infos.put("微信APP-SECRET", environment.getProperty("resourcecatalog.wx.app-secret"));
//        infos.put("微信支付MCH-ID", environment.getProperty("resourcecatalog.wx.mch-id"));
//        infos.put("微信支付MCH-KEY", environment.getProperty("resourcecatalog.wx.mch-key"));
//        infos.put("微信支付通知地址", environment.getProperty("resourcecatalog.wx.notify-url"));
//
//        //测试获取System表配置信息
//        infos.put(SystemInfoPrinter.CREATE_PART_COPPER + 3, "系统设置");
//
//        return infos;
//    }
//}
