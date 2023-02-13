package d1.project.dcrun.center.webapi.dcapi.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.project.dcrun.center.webapi.common.service.mqtt.MqttClient;
import d1.project.dcrun.center.webapi.common.service.operation.record.OperationRecordService;
import d1.project.dcrun.center.webapi.common.service.system.SysService;
import d1.project.dcrun.center.webapi.common.service.system.SysServiceService;
import d1.project.dcrun.center.webapi.common.util.MyPropertiesUtil;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import d1.project.dcrun.center.webapi.common.util.OperationType;
import d1.project.dcrun.center.webapi.common.util.ServiceStatus;
import d1.project.dcrun.center.webapi.dcapi.utils.DCApiUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

/**
 * @Author maoyy
 */
@Service
public class ApiConfigService {

    @Autowired
    private SysServiceService sysServiceService;

    @Autowired
    private MqttClient mqttClient;

    @Autowired
    private OperationRecordService operationRecordService;

    @Value("${file.server.uri}")
    private String fileServerUri;

    /**
     * 获取Api集成服务application.properties文件内容
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Object findAll(JSONObject params) throws Exception {
        String appid = params.getString("appid");

        SysService sysService = sysServiceService.findById(appid);

        HashMap<String, String> list = new HashMap<>(4);
        String propertiesPath = DCApiUtils.getApplicationPropertiesPath(sysService.getIntegrationId(), sysService.getId());

        Properties p = new Properties();
        InputStream inputStream = new BufferedInputStream(new FileInputStream(propertiesPath));
        //从输入流中读取属性列表（键和元素对）
        p.load(inputStream);
        //得到配置文件的名字
        Enumeration en = p.propertyNames();
        while (en.hasMoreElements()) {
            String strKey = (String) en.nextElement();
            String strValue = p.getProperty(strKey);
            list.put(strKey, strValue);
        }

        return list;
    }

    /**
     * 更新api集成服务数据库配置
     *
     * @param params
     * @throws Exception
     */
    public void update(JSONObject params) throws Exception {
        String id = params.getString("appid");
        String url = MyUtils.eliminateSpaces(params.getString("url"));
        String username = params.getString("username");
        String password = params.getString("password");

        SysService sysService = sysServiceService.findById(id);

        String propertiesPath = DCApiUtils.getApplicationPropertiesPath(sysService.getIntegrationId(), sysService.getId());
        Properties p = MyPropertiesUtil.init(propertiesPath);
        MyPropertiesUtil.update("spring.datasource.url", url, propertiesPath, p);
        MyPropertiesUtil.update("spring.datasource.username", username, propertiesPath, p);
        MyPropertiesUtil.update("spring.datasource.password", password, propertiesPath, p);
    }

    /**
     * 同步dcapigateway数据库配置信息
     *
     * @param params
     * @throws Exception
     */
    public void sync(JSONObject params) throws Exception {
        String appid = params.getString("appid");

        Boolean isInstall = sysServiceService.isInstall(appid);
        if (!isInstall) {
            throw new ValidException("系统服务未安装，不允许同步配置信息");
        }
        SysService sysService = sysServiceService.findById(appid);

        if (!ServiceStatus.ONLINE.getName().equals(sysService.getStatus())) {
            throw new ValidException("系统服务离线，不允许同步配置信息");
        }

        File propertiesFile = new File(DCApiUtils.getApplicationPropertiesPath(sysService.getIntegrationId(), sysService.getId()));
        File outputFile = new File(DCApiUtils.getApplicationPropertiesOutputPath(appid));

        // 拷贝文件
        FileUtils.copyFile(propertiesFile, outputFile);

        //保存到数据库
        String operationId = operationRecordService.insert(sysService, OperationType.SYNC);

        String topic = DCApiUtils.getSyncTopic(sysService.getIntegrationId(), sysService.getRunNodeId(), sysService.getId());
        String url = fileServerUri + "downloadDcapiConfig/" + outputFile.getName();
        // 发送MQTT，系统服务
        mqttClient.publish(topic, sysService.getId(), url, "", operationId);


    }
}
