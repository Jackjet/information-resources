package d1.project.dcrun.center.webapi.datacache.service;

import com.alibaba.fastjson.JSONObject;
import d1.project.dcrun.center.webapi.common.service.mqtt.MqttClient;
import d1.project.dcrun.center.webapi.common.service.operation.record.OperationRecordService;
import d1.project.dcrun.center.webapi.common.service.system.SysService;
import d1.project.dcrun.center.webapi.common.service.system.SysServiceService;
import d1.project.dcrun.center.webapi.common.util.Constants;
import d1.project.dcrun.center.webapi.common.util.OperationType;
import d1.project.dcrun.center.webapi.common.util.ServiceStatus;
import d1.project.dcrun.center.webapi.datacache.utils.DataCacheUtils;
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
public class DataCacheService {
    @Autowired
    private SysServiceService sysServiceService;

    @Autowired
    private MqttClient mqttClient;

    @Autowired
    private OperationRecordService operationRecordService;

    @Value("${file.server.uri}")
    private String fileServerUri;

    @Autowired
    private ICompressService compressService;

    public void install(JSONObject params) throws Exception {
        String appid = params.getString("appid");
        SysService sysService = sysServiceService.findById(appid);

        // 上传文件
        String rootPath = DataCacheUtils.getServiceRootPath(sysService.getIntegrationId(), appid);

        File outputPath = new File(Constants.FILES_DOWNLOAD_DATACACHE, appid + ".zip");

        File rootPathFile = new File(rootPath);
        List<String> paths = Arrays.stream(rootPathFile.listFiles()).map(tempFile -> tempFile.getPath()).collect(Collectors.toList());
        compressService.zipFiles(paths, outputPath.getAbsolutePath());

        String operationId = operationRecordService.insert(sysService, OperationType.INSTALL);

        String url = fileServerUri + "downloadDataCache/" + outputPath.getName();
        String topic = DataCacheUtils.getInstallTopic(sysService.getIntegrationId(), sysService.getRunNodeId(), sysService.getId());
        mqttClient.publish(topic, sysService.getId(), url, "", operationId);

        sysService.setStatus(ServiceStatus.INSTALLING.getName());
        sysServiceService.insert(sysService);
    }
}
