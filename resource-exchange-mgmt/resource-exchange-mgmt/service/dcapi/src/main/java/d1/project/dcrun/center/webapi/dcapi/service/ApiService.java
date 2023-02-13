package d1.project.dcrun.center.webapi.dcapi.service;

import com.alibaba.fastjson.JSONObject;
import d1.project.dcrun.center.webapi.common.service.mqtt.MqttClient;
import d1.project.dcrun.center.webapi.common.service.operation.record.OperationRecordService;
import d1.project.dcrun.center.webapi.common.service.system.SysService;
import d1.project.dcrun.center.webapi.common.service.system.SysServiceService;
import d1.project.dcrun.center.webapi.common.util.Constants;
import d1.project.dcrun.center.webapi.common.util.OperationType;
import d1.project.dcrun.center.webapi.common.util.ServiceStatus;
import d1.project.dcrun.center.webapi.dcapi.utils.DCApiUtils;
import net.dcrun.component.compression.ICompressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author maoyy
 */
@Service
public class ApiService {
    @Autowired
    private SysServiceService sysServiceService;

    @Autowired
    private MqttClient mqttClient;

    @Value("${file.server.uri}")
    private String fileServerUri;

    @Autowired
    private OperationRecordService operationRecordService;

    @Autowired
    private ICompressService compressService;

    public void install(JSONObject params) throws Exception {
        String appid = params.getString("appid");
        SysService sysService = sysServiceService.findById(appid);

        // 上传文件
        String rootPath = DCApiUtils.getServiceRootPath(sysService.getIntegrationId(), appid);

        File outputPath = new File(Constants.FILES_DOWNLOAD_DCAPI, appid + ".zip");

        File rootPathFile = new File(rootPath);
        List<String> paths = Arrays.stream(rootPathFile.listFiles()).map(tempFile -> tempFile.getPath()).collect(Collectors.toList());
        compressService.zipFiles(paths, outputPath.getAbsolutePath());

        String url = fileServerUri + "downloadApiGateway/" + outputPath.getName();

        // 发送MQTT，系统服务
        String topic = DCApiUtils.getInstallTopic(sysService.getIntegrationId(), sysService.getRunNodeId(), sysService.getId());

        String operationId = operationRecordService.insert(sysService, OperationType.INSTALL);
        mqttClient.publish(topic, sysService.getId(), url, "", operationId);

        sysService.setStatus(ServiceStatus.INSTALLING.getName());
        sysServiceService.insert(sysService);
    }
}
