package d1.project.dcrun.center.webapi.system.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.TokenService;
import d1.project.dcrun.center.webapi.backup.service.BackupService;
import d1.project.dcrun.center.webapi.common.service.emq.EmqBaseService;
import d1.project.dcrun.center.webapi.common.service.keydict.KeyDictService;
import d1.project.dcrun.center.webapi.common.service.management.config.ManagementConfig;
import d1.project.dcrun.center.webapi.common.service.management.config.ManagementConfigService;
import d1.project.dcrun.center.webapi.common.service.mqtt.MqttClient;
import d1.project.dcrun.center.webapi.common.service.operation.record.OperationRecordService;
import d1.project.dcrun.center.webapi.common.service.system.SysService;
import d1.project.dcrun.center.webapi.common.service.system.SysServiceService;
import d1.project.dcrun.center.webapi.common.util.*;
import d1.project.dcrun.center.webapi.dcapi.utils.DCApiUtils;
import d1.project.dcrun.center.webapi.system.entity.RunNode;
import d1.project.dcrun.center.webapi.system.model.CoreSysServiceServiceInfoVm;
import d1.project.dcrun.center.webapi.system.service.factory.AbstractSystemService;
import d1.project.dcrun.center.webapi.system.service.factory.ServicesFactory;
import net.dcrun.component.compression.ICompressService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xuaa
 */
@Service
public class CoreSysServiceService {
    private final ICompressService compressService;
    private final SysServiceService sysServiceService;
    private final ManagementConfigService managementConfigService;
    private final MqttClient mqttClient;
    private final OperationRecordService operationRecordService;
    private final TokenService tokenService;
    private final ServicesFactory servicesFactory;
    private final KeyDictService keyDictService;
    private final RunNodeService runNodeService;
    private final EmqBaseService emqBaseService;
    private final SysInstallPackService sysInstallPackService;
    private final BackupService backupService;
    @Value("${file.server.uri}")
    private String fileServerUri;

    @Autowired
    public CoreSysServiceService(ICompressService compressService,
                                 SysServiceService sysServiceService,
                                 ManagementConfigService managementConfigService,
                                 MqttClient mqttClient,
                                 OperationRecordService operationRecordService,
                                 TokenService tokenService,
                                 KeyDictService keyDictService,
                                 RunNodeService runNodeService,
                                 ServicesFactory servicesFactory,
                                 SysInstallPackService sysInstallPackService,
                                 EmqBaseService emqBaseService,
                                 BackupService backupService) {
        this.compressService = compressService;
        this.sysServiceService = sysServiceService;
        this.managementConfigService = managementConfigService;
        this.mqttClient = mqttClient;
        this.operationRecordService = operationRecordService;
        this.tokenService = tokenService;
        this.servicesFactory = servicesFactory;
        this.keyDictService = keyDictService;
        this.runNodeService = runNodeService;
        this.sysInstallPackService = sysInstallPackService;
        this.emqBaseService = emqBaseService;
        this.backupService = backupService;
    }

    /**
     * ??????????????????
     *
     * @param params ????????????
     * @return ????????????
     */
    public Page<SysService> findAll(JSONObject params) throws Exception {
        return sysServiceService.findAll(params);
    }

    /**
     * ??????????????????
     *
     * @param params  ????????????
     * @param request ????????????
     * @throws Exception ????????????
     */
    public void insert(JSONObject params, HttpServletRequest request) throws Exception {
        SysService sysService = JSONObject.parseObject(params.toJSONString(), SysService.class);
        // ???????????????????????????
        RunNode runNode = runNodeService.findById(sysService.getRunNodeId());
        if (runNode == null) {
            throw new ValidException("?????????????????????");
        }
        if (runNode.getReportTime() == null || !MyUtils.isOnline(runNode.getReportTime().getTime())) {
            throw new ValidException("?????????????????????");
        }

        // ???????????????
        verify(sysService);

        // id???appkey????????????8??????????????????????????????
        String id = keyDictService.generateAppId();
        String appkey = MyUtils.generatePrimaryKeyId();
        sysService.setId(id);
        sysService.setAppkey(appkey);
        sysService.setType("java");

        tokenService.updateCreateIdAndTime(request, sysService);

        // ????????????????????????????????????
        String rootPath = Constants.getServiceRootPath(sysService.getIntegrationId(), sysService.getTemplateType(), id);
        File rootPathFile = new File(rootPath);
        rootPathFile.mkdirs();

        // ????????????????????????????????????service??????
        AbstractSystemService systemService = servicesFactory.getSystemService(sysService.getTemplateType());

        // ??????????????????????????????
        systemService.createBaseFiles(sysService, rootPath, sysInstallPackService.findById(sysService.getTemplateId()));

        // ??????config????????????
        systemService.createConfigFiles(sysService.getIntegrationId(), sysService.getTemplateType(), id, appkey);

        // ????????????
        systemService.specialOperations(sysService);

        // ??????appid???appkey?????????emq??????????????????topic??????
        systemService.createEmqUserAndAuthorizePermission(id, appkey, sysService.getIntegrationId(), sysService.getTemplateType());

        sysService.setNodeName(runNodeService.getRunNodeName(sysService.getRunNodeId()));
        sysServiceService.insert(sysService);
    }

    /**
     * ??????????????????url???????????????url???????????????????????????
     *
     * @throws Exception ????????????
     */
    private void verify(SysService sysService) throws Exception {

        String name = sysService.getName();
        String runNodeId = sysService.getRunNodeId();

        //??????????????????url???????????????url????????????????????????
        ManagementConfig url = managementConfigService.findByConfigKey("manage.center.url");
        if (url == null || StringUtils.isEmpty(url.getConfigValue())) {
            throw new ValidException("??????????????????????????????");
        }
        ManagementConfig mqUrl = managementConfigService.findByConfigKey("manage.center.mq.url");
        if (mqUrl == null || StringUtils.isEmpty(mqUrl.getConfigValue())) {
            throw new ValidException("????????????????????????");
        }
        if (sysServiceService.existsByNameAndRunNodeId(name, runNodeId)) {
            throw new ValidException("??????????????????????????????");
        }
    }

    /**
     * ??????????????????
     *
     * @param params  ????????????
     * @param request ????????????
     * @throws Exception ????????????
     */
    public void update(JSONObject params, HttpServletRequest request) throws Exception {
        SysService sysService = JSONObject.parseObject(params.toJSONString(), SysService.class);
        SysService old = sysServiceService.findById(sysService.getId());
        // ????????????????????????????????????service??????
        AbstractSystemService systemService = servicesFactory.getSystemService(old.getTemplateType());
        systemService.updateVerify(sysServiceService, old, params);

        old.setName(sysService.getName());
        old.setRemark(sysService.getRemark());
        old.setUrl(MyUtils.eliminateSpaces(sysService.getUrl()));
        tokenService.updateUpdateIdAndTime(request, old);
        sysServiceService.insert(old);

        //????????????
        JSONObject json = new JSONObject();
        json.put("integrationId", old.getIntegrationId());
        json.put("id", old.getId());
        String port = params.getString("serverPort");
        json.put("port", port);
        systemService.updateSpecialOperations(json);
    }

    /**
     * ??????????????????
     *
     * @param id ????????????Id
     * @return ????????????
     * @throws Exception ????????????
     */
    public JSONObject findByIdDetails(String id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            throw new ValidException("????????????appid????????????");
        }
        SysService sysService = sysServiceService.findById(id);
        JSONObject result = (JSONObject) JSONObject.toJSON(sysService);
        result.put("createTime", sysService.getCreateTime());
        if (ServiceType.DCAPI.getName().equals(sysService.getTemplateType())) {
            HashMap<String, String> list = new HashMap<>(4);
            String propertiesPath = DCApiUtils.getApplicationPropertiesPath(sysService.getIntegrationId(), sysService.getId());

            Properties p = new Properties();
            InputStream inputStream = new BufferedInputStream(new FileInputStream(propertiesPath));
            //??????????????????????????????????????????????????????
            p.load(inputStream);
            //???????????????????????????
            Enumeration en = p.propertyNames();
            while (en.hasMoreElements()) {
                String strKey = (String) en.nextElement();
                String strValue = p.getProperty(strKey);
                result.put(strKey, strValue);
            }
        }
        return result;
    }

    /**
     * ??????????????????
     * * ????????????files/services/????????????appid??????(??????????????????appid??????)
     *
     * @param id appId
     * @throws Exception ????????????
     */
    public void deleteById(String id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            throw new ValidException("????????????appid????????????");
        }

        SysService sysService = sysServiceService.findById(id);

        //???????????????
        String rootPath = Constants.getServiceRootPath(sysService.getIntegrationId(), sysService.getTemplateType(), id);
        File rootDirectory = new File(rootPath);
        FileUtils.deleteDirectory(rootDirectory);

        if (sysService.getStatus() != null) {
            // ??????MQTT?????????????????????
            String topic = sysService.getIntegrationId() + "/" + sysService.getRunNodeId() + "/" + sysService.getTemplateType() + "/" + sysService.getId() + "/" + OperationType.DELETE.getName();
            String operationId = operationRecordService.insert(sysService, OperationType.DELETE);
            mqttClient.publish(topic, sysService.getId(), "", "", operationId);
        }

        //????????????
        sysServiceService.deleteById(id);

        // ??????mqtt???????????????
        List<String> userList = new ArrayList<>();
        userList.add(id);
        emqBaseService.deletePermissionAndUser(userList);
    }


    /**
     * ??????????????????????????????
     *
     * @param params ????????????
     */
    public boolean isOnline(JSONObject params) throws Exception {
        SysService sysService = sysServiceService.findById(params.getString("appid"));
        if (ServiceStatus.ONLINE.getName().equals(sysService.getStatus())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ??????????????????
     *
     * @param params ????????????
     */
    public void install(JSONObject params) throws Exception {
        String appid = params.getString("appid");
        SysService sysService = sysServiceService.findById(appid);

        // ????????????
        String rootPath = Constants.getServiceRootPath(sysService.getIntegrationId(), sysService.getTemplateType(), appid);
        String downloadPath = Constants.getDownloadPathByType(sysService.getTemplateType());
        String fileName = appid + "_" + System.currentTimeMillis() + ".zip";

        File rootPathFile = new File(rootPath);
        List<String> paths = Arrays.stream(rootPathFile.listFiles()).map(tempFile -> tempFile.getPath()).collect(Collectors.toList());
        compressService.zipFiles(paths, downloadPath + fileName);

        String url = fileServerUri + "downloadWebapiInstallPackage/" + fileName;
        // ??????MQTT???????????????
        String topic = sysService.getIntegrationId() + "/" + sysService.getRunNodeId() + "/" + sysService.getTemplateType() + "/" + sysService.getId() + "/" + OperationType.INSTALL.getName();

        String operationId = operationRecordService.insert(sysService, OperationType.INSTALL);
        mqttClient.publish(topic, sysService.getId(), url, "", operationId);

        sysService.setStatus(ServiceStatus.INSTALLING.getName());
        sysServiceService.insert(sysService);
    }

    /**
     * ??????????????????id????????????????????????/???????????????/??????
     *
     * @param params
     * @return
     * @throws Exception
     */
    public CoreSysServiceServiceInfoVm versionAndStatus(JSONObject params) throws Exception {
        String appid = params.getString("appid");
        SysService sysService = sysServiceService.findById(appid);
        CoreSysServiceServiceInfoVm coreSysServiceServiceInfoVm = new CoreSysServiceServiceInfoVm();

        File versionFile = new File(Constants.getServiceRootPath(sysService.getIntegrationId(), sysService.getTemplateType(), appid), "version");
        String currentVersion = FileUtils.readFileToString(versionFile, StandardCharsets.UTF_8);
        if (StringUtils.isEmpty(sysService.getVersion())) {
            throw new ValidException("??????????????????");
        }
        String remoteVersion = sysService.getVersion().split(",")[0];

        coreSysServiceServiceInfoVm.setCurrentVersion(currentVersion);
        coreSysServiceServiceInfoVm.setRemoteVersion(remoteVersion);
        coreSysServiceServiceInfoVm.setStatus(sysService.getStatus());

        return coreSysServiceServiceInfoVm;
    }

    /**
     * ????????????
     *
     * @param params ????????????appid
     * @throws Exception ????????????
     */
    @Transactional(rollbackFor = Exception.class)
    public void codeBackup(JSONObject params,HttpServletRequest request) throws Exception {
        String appid = params.getString("appid");
        SysService sysService = sysServiceService.findById(appid);

        // ????????????
        File scriptRootPath = new File(Constants.getSysServiceScriptPath(sysService.getIntegrationId(), sysService.getTemplateType(), sysService.getId()));

        long version = System.currentTimeMillis();

        List<String> paths = new ArrayList<>();
        File[] listFiles = scriptRootPath.listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            paths.add(listFiles[i].getAbsolutePath());
        }

        String fileName = sysService.getId() + "_" + version + ".zip";
        compressService.zipFiles(paths, Constants.getDownloadPathByType(sysService.getTemplateType()) + fileName);

        String url = fileServerUri + "downloadCustomConfigZip/" + fileName;

        JSONObject backUpParam = new JSONObject();
        backUpParam.put("name",params.get("name"));
        backUpParam.put("url",url);
//        ????????????????????????
        backupService.insert(request,backUpParam);

    }

    /**
     * ??????????????????????????????????????????
     *
     * @param params ????????????appid
     * @throws Exception ????????????
     */
    public void upgradeConfig(JSONObject params) throws Exception {
        String appid = params.getString("appid");
        SysService sysService = sysServiceService.findById(appid);

        Boolean isInstall = sysServiceService.isInstall(appid);
        if (!isInstall) {
            throw new ValidException("?????????????????????????????????????????????");
        }

        if (!ServiceStatus.ONLINE.getName().equals(sysService.getStatus())) {
            throw new ValidException("??????????????????????????????????????????");
        }

        // ????????????
        File configRootPath = new File(Constants.getSysServiceConfigPath(sysService.getIntegrationId(), sysService.getTemplateType(), sysService.getId()));

        long version = System.currentTimeMillis();
        // ????????????config?????????
        File configTimestampFile = new File(configRootPath, "timestamp");
        FileUtils.write(configTimestampFile, String.valueOf(version), StandardCharsets.UTF_8);

        List<String> paths = new ArrayList<>();
        File[] listFiles = configRootPath.listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            paths.add(listFiles[i].getAbsolutePath());
        }

        String fileName = sysService.getId() + "_" + version + ".zip";
        compressService.zipFiles(paths, Constants.getDownloadPathByType(sysService.getTemplateType()) + fileName);

        String url = fileServerUri + "downloadCustomConfigZip/" + fileName;
        String topic = sysService.getIntegrationId() + "/" + sysService.getRunNodeId() + "/" + sysService.getTemplateType() + "/" + sysService.getId() + "/" + OperationType.SYNC.getName();

        String operationId = operationRecordService.insert(sysService, OperationType.SYNC);
        mqttClient.publish(topic, appid, url, String.valueOf(version), operationId);
    }

    /**
     * ????????????????????????????????????
     *
     * @param appid ????????????appid
     * @throws Exception ????????????
     */
    public void upgradeScript(String appid) throws Exception {
        SysService sysService = sysServiceService.findById(appid);

        Boolean isInstall = sysServiceService.isInstall(appid);
        if (!isInstall) {
            throw new Exception("?????????????????????????????????????????????");
        }

        if (!ServiceStatus.ONLINE.getName().equals(sysService.getStatus())) {
            throw new Exception("??????????????????????????????????????????");
        }

        // ????????????
        File scriptRootPath = new File(Constants.getSysServiceScriptPath(sysService.getIntegrationId(), sysService.getTemplateType(), sysService.getId()));

        long version = System.currentTimeMillis();
        // ????????????script?????????
        File scriptTimestampFile = new File(scriptRootPath, "timestamp");
        FileUtils.write(scriptTimestampFile, String.valueOf(version), StandardCharsets.UTF_8);

        List<String> paths = new ArrayList<>();
        File[] listFiles = scriptRootPath.listFiles();
        for (int i = 0; i < listFiles.length; i++) {
            paths.add(listFiles[i].getAbsolutePath());
        }

        String fileName = sysService.getId() + "_" + version + ".zip";
        compressService.zipFiles(paths, Constants.getDownloadPathByType(sysService.getTemplateType()) + fileName);

        String url = fileServerUri + "downloadCustomConfigZip/" + fileName;
        String topic = sysService.getIntegrationId() + "/" + sysService.getRunNodeId() + "/" + sysService.getTemplateType() + "/" + sysService.getId() + "/" + OperationType.SYNC_SCRIPT.getName();

        String operationId = operationRecordService.insert(sysService, OperationType.SYNC_SCRIPT);
        mqttClient.publish(topic, appid, url, String.valueOf(version), operationId);
    }
}
