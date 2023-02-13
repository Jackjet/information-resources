package d1.project.dcrun.center.webapi.emq.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.project.dcrun.center.webapi.common.service.mqtt.MqttClient;
import d1.project.dcrun.center.webapi.common.service.operation.record.OperationRecordService;
import d1.project.dcrun.center.webapi.common.service.system.SysService;
import d1.project.dcrun.center.webapi.common.service.system.SysServiceService;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import d1.project.dcrun.center.webapi.common.util.OperationType;
import d1.project.dcrun.center.webapi.common.util.ServiceStatus;
import d1.project.dcrun.center.webapi.emq.utils.EmqUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author maoyy
 */
@Service
public class EmqConfigService {
    @Value("${file.server.uri}")
    private String fileServerUri;

    @Autowired
    private SysServiceService sysServiceService;
    @Autowired
    private MqttClient mqttClient;
    @Autowired
    private OperationRecordService operationRecordService;

    /**
     * 消息集成服务数据库配置信息
     *
     * @param params 查询列表
     * @return 返回结果
     * @throws Exception
     */
    public JSONObject findAll(JSONObject params) throws Exception {
        String appid = params.getString("appid");

        SysService sysService = sysServiceService.findById(appid);
        File configFile = new File(EmqUtils.getConfigJsonPath(sysService.getIntegrationId(), appid));

        String text = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
        return JSONObject.parseObject(text);
    }

    /**
     * 系统服务数据库配置编辑
     *
     * @param params
     * @throws Exception
     */
    public void update(JSONObject params) throws Exception {
        String appid = params.getString("appid");
        SysService sysService = sysServiceService.findById(appid);

        JSONObject dataJson = params.getJSONObject("data");
        JSONObject result = new JSONObject();
        // 去除json中id和key两边可能存在的空格
        for (Map.Entry<String, Object> entry : dataJson.entrySet()) {
            result.put(MyUtils.eliminateSpaces(entry.getKey()), MyUtils.eliminateSpaces((String) entry.getValue()));
        }

        // 写入config.json配置文件
        FileUtils.write(new File(EmqUtils.getConfigJsonPath(sysService.getIntegrationId(), appid)), result.toJSONString(), StandardCharsets.UTF_8);
    }

    public void syncEmqConfig(JSONObject params) throws Exception {
        String appid = params.getString("appid");

        Boolean isInstall = sysServiceService.isInstall(appid);
        if (!isInstall) {
            throw new ValidException("系统服务未安装，不允许同步数据库信息");
        }

        SysService sysService = sysServiceService.findById(appid);

        if (!ServiceStatus.ONLINE.getName().equals(sysService.getStatus())) {
            throw new ValidException("系统服务离线，不允许同步数据库信息");
        }

        // 上传文件
        File configPath = new File(EmqUtils.getConfigJsonPath(sysService.getIntegrationId(), sysService.getId()));
        File outputFile = new File(EmqUtils.getConfigJsonOutputPath(appid));

        // 拷贝文件
        FileUtils.copyFile(configPath, outputFile);

        //保存到数据库
        String operationId = operationRecordService.insert(sysService, OperationType.SYNC);

        String topic = EmqUtils.getSyncTopic(sysService.getIntegrationId(), sysService.getRunNodeId(), sysService.getId());
        String url = fileServerUri + "downloadEmqConfig/" + outputFile.getName();
        mqttClient.publish(topic, sysService.getId(), url, "", operationId);
    }
}
