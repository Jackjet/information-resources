package d1.project.dcrun.center.webapi;

import com.alibaba.fastjson.JSONObject;
import d1.project.dcrun.center.webapi.common.CommonFunction;
import d1.project.dcrun.center.webapi.common.service.management.config.ManagementConfig;
import d1.project.dcrun.center.webapi.common.service.management.config.ManagementConfigDao;
import d1.project.dcrun.center.webapi.common.util.Constants;
import d1.project.dcrun.center.webapi.common.util.ServiceType;
import d1.project.dcrun.center.webapi.system.service.CoreSysServiceService;
import d1.project.dcrun.center.webapi.system.service.RunNodeService;
import net.dcrun.component.mqtt.client.IMqttClientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * 服务启动成功后，订阅MQTT主题类
 *
 * @Author chengh
 **/
@Component
public class MyAppRunner implements ApplicationRunner {
    private static Logger logger = LoggerFactory.getLogger(MyAppRunner.class);

    @Value("${d1.project.mqtt.url}")
    private String url;
    @Value("${d1.project.mqtt.name}")
    private String name;
    @Value("${d1.project.mqtt.password}")
    private String password;
    @Autowired
    private CommonFunction commonService;
    @Autowired
    private CoreSysServiceService coreSysServiceService;
    @Autowired
    private ManagementConfigDao managementConfigDao;
    @Autowired
    private IMqttClientService mqttClientService;
    @Autowired
    private RunNodeService runNodeService;

    /**
     * 启动成功后执行此方法
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 获取心跳MQTT指令，更新节点基础服务上报时间, top: 集成项目id/节点ID/node/节点ID/ping
        mqttClientService.on("+/+/" + ServiceType.NODE.getName() + "/+/ping", (topic, msg) -> {
            try {
                runNodeService.ping(msg);
            } catch (Exception e) {
                logger.error("subscribe topic ping failed", e);
            }
        });

        // 连接操作，此操作放在最后一行
        mqttClientService.connect2(url, name, password);

        commonService.setGetAppkeyByAppidFunction(appid -> {
            try {
                JSONObject sysService = coreSysServiceService.findByIdDetails(appid);
                if (sysService == null) {
                    return null;
                }
                return sysService.getString("appkey");
            } catch (Exception e) {
                logger.error("setGetAppkeyByAppidFunction failed", e);
            }
            return null;
        });

        commonService.setGetHostsSupplier(() -> {
            ManagementConfig managementConfig = this.managementConfigDao.findByConfigKey("manage.center.url");
            return managementConfig.getConfigValue();
        });

        // 创建默认文件夹
        new File(Constants.TEMP_ROOT).mkdirs();
        new File(Constants.FILES_BASIC_NODE_INSTALL).mkdirs();
        new File(Constants.FILES_BASIC_WEBAPI_SOURCE).mkdirs();
        new File(Constants.FILES_BASIC_WEBAPI_INSTALL).mkdirs();
        new File(Constants.FILES_BASIC_WEBAPI_COMPONENTS).mkdirs();
        new File(Constants.FILES_BASIC_EMQ).mkdirs();
        new File(Constants.FILES_BASIC_DCAPI_INSTALL).mkdirs();
        new File(Constants.FILES_BASIC_DATACACHE_INSTALL).mkdirs();
        new File(Constants.FILES_SERVICE).mkdirs();
        new File(Constants.FILES_DOWNLOAD_NODE).mkdirs();
        new File(Constants.FILES_DOWNLOAD_WEBAPI).mkdirs();
        new File(Constants.FILES_DOWNLOAD_EMQ).mkdirs();
        new File(Constants.FILES_DOWNLOAD_DCAPI).mkdirs();
        new File(Constants.FILES_DOWNLOAD_DATACACHE).mkdirs();
        new File(Constants.FILES_BUILD_WEBAPI_TEMP).mkdirs();
    }
}
