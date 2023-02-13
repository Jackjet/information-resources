package d1.project.dcrun.center.webapi.emq.service;

import com.alibaba.fastjson.JSONObject;
import d1.project.dcrun.center.webapi.common.service.mqtt.MqttClient;
import d1.project.dcrun.center.webapi.common.service.operation.record.OperationRecordService;
import d1.project.dcrun.center.webapi.common.service.system.SysService;
import d1.project.dcrun.center.webapi.common.service.system.SysServiceService;
import d1.project.dcrun.center.webapi.common.util.OperationType;
import d1.project.dcrun.center.webapi.common.util.ServiceStatus;
import d1.project.dcrun.center.webapi.emq.utils.EmqUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * @author maoyy
 */
@Service
public class EmqSysService {
    @Value("${file.server.uri}")
    private String fileServerUri;

    @Autowired
    private SysServiceService sysServiceService;
    @Autowired
    private MqttClient mqttClient;
    @Autowired
    private OperationRecordService operationRecordService;


    public void install(JSONObject params) throws Exception {
        String appid = params.getString("appid");
        SysService sysService = sysServiceService.findById(appid);

        // 上传文件
        File configPath = new File(EmqUtils.getConfigJsonPath(sysService.getIntegrationId(), sysService.getId()));
        File outputFile = new File(EmqUtils.getConfigJsonOutputPath(appid));

        // 拷贝文件
        FileUtils.copyFile(configPath, outputFile);

        //保存到数据库
        String operationId = operationRecordService.insert(sysService, OperationType.INSTALL);

        String topic = EmqUtils.getInstallTopic(sysService.getIntegrationId(), sysService.getRunNodeId(), sysService.getId());
        String url = fileServerUri + "downloadEmqConfig/" + outputFile.getName();
        mqttClient.publish(topic, sysService.getId(), url, "", operationId);

        sysService.setStatus(ServiceStatus.INSTALLING.getName());
        sysServiceService.insert(sysService);
    }
}
