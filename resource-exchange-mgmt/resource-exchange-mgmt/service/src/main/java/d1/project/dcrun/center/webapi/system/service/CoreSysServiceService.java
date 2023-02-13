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
     * 系统服务列表
     *
     * @param params 查询列表
     * @return 返回结果
     */
    public Page<SysService> findAll(JSONObject params) throws Exception {
        return sysServiceService.findAll(params);
    }

    /**
     * 新增系统服务
     *
     * @param params  添加实体
     * @param request 当前用户
     * @throws Exception 抛出异常
     */
    public void insert(JSONObject params, HttpServletRequest request) throws Exception {
        SysService sysService = JSONObject.parseObject(params.toJSONString(), SysService.class);
        // 检查所属节点的状态
        RunNode runNode = runNodeService.findById(sysService.getRunNodeId());
        if (runNode == null) {
            throw new ValidException("所属节点不存在");
        }
        if (runNode.getReportTime() == null || !MyUtils.isOnline(runNode.getReportTime().getTime())) {
            throw new ValidException("当前节点已离线");
        }

        // 验证合法性
        verify(sysService);

        // id及appkey自动生成8位，数字加字母混合；
        String id = keyDictService.generateAppId();
        String appkey = MyUtils.generatePrimaryKeyId();
        sysService.setId(id);
        sysService.setAppkey(appkey);
        sysService.setType("java");

        tokenService.updateCreateIdAndTime(request, sysService);

        // 创建系统服务文件夹根路径
        String rootPath = Constants.getServiceRootPath(sysService.getIntegrationId(), sysService.getTemplateType(), id);
        File rootPathFile = new File(rootPath);
        rootPathFile.mkdirs();

        // 根据模板类型，获取不同的service对象
        AbstractSystemService systemService = servicesFactory.getSystemService(sysService.getTemplateType());

        // 创建系统服务基础文件
        systemService.createBaseFiles(sysService, rootPath, sysInstallPackService.findById(sysService.getTemplateId()));

        // 创建config文件内容
        systemService.createConfigFiles(sysService.getIntegrationId(), sysService.getTemplateType(), id, appkey);

        // 其他处理
        systemService.specialOperations(sysService);

        // 根据appid和appkey，创建emq用户，并授予topic权限
        systemService.createEmqUserAndAuthorizePermission(id, appkey, sysService.getIntegrationId(), sysService.getTemplateType());

        sysService.setNodeName(runNodeService.getRunNodeName(sysService.getRunNodeId()));
        sysServiceService.insert(sysService);
    }

    /**
     * 如果管理中心url和消息队列url没有值创建不成功；
     *
     * @throws Exception 抛出异常
     */
    private void verify(SysService sysService) throws Exception {

        String name = sysService.getName();
        String runNodeId = sysService.getRunNodeId();

        //如果管理中心url和消息队列url没有值创建不成功
        ManagementConfig url = managementConfigService.findByConfigKey("manage.center.url");
        if (url == null || StringUtils.isEmpty(url.getConfigValue())) {
            throw new ValidException("管理中心服务值不存在");
        }
        ManagementConfig mqUrl = managementConfigService.findByConfigKey("manage.center.mq.url");
        if (mqUrl == null || StringUtils.isEmpty(mqUrl.getConfigValue())) {
            throw new ValidException("消息队列值不存在");
        }
        if (sysServiceService.existsByNameAndRunNodeId(name, runNodeId)) {
            throw new ValidException("该系统服务名称已存在");
        }
    }

    /**
     * 系统服务编辑
     *
     * @param params  修改实体
     * @param request 当前用户
     * @throws Exception 抛出异常
     */
    public void update(JSONObject params, HttpServletRequest request) throws Exception {
        SysService sysService = JSONObject.parseObject(params.toJSONString(), SysService.class);
        SysService old = sysServiceService.findById(sysService.getId());
        // 根据模板类型，获取不同的service对象
        AbstractSystemService systemService = servicesFactory.getSystemService(old.getTemplateType());
        systemService.updateVerify(sysServiceService, old, params);

        old.setName(sysService.getName());
        old.setRemark(sysService.getRemark());
        old.setUrl(MyUtils.eliminateSpaces(sysService.getUrl()));
        tokenService.updateUpdateIdAndTime(request, old);
        sysServiceService.insert(old);

        //特殊操作
        JSONObject json = new JSONObject();
        json.put("integrationId", old.getIntegrationId());
        json.put("id", old.getId());
        String port = params.getString("serverPort");
        json.put("port", port);
        systemService.updateSpecialOperations(json);
    }

    /**
     * 系统服务详情
     *
     * @param id 系统服务Id
     * @return 返回结果
     * @throws Exception 抛出异常
     */
    public JSONObject findByIdDetails(String id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            throw new ValidException("系统服务appid不能为空");
        }
        SysService sysService = sysServiceService.findById(id);
        JSONObject result = (JSONObject) JSONObject.toJSON(sysService);
        result.put("createTime", sysService.getCreateTime());
        if (ServiceType.DCAPI.getName().equals(sysService.getTemplateType())) {
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
                result.put(strKey, strValue);
            }
        }
        return result;
    }

    /**
     * 删除系统服务
     * * 需要删除files/services/系统服务appid目录(包括系统服务appid目录)
     *
     * @param id appId
     * @throws Exception 抛出异常
     */
    public void deleteById(String id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            throw new ValidException("系统服务appid不能为空");
        }

        SysService sysService = sysServiceService.findById(id);

        //删除文件夹
        String rootPath = Constants.getServiceRootPath(sysService.getIntegrationId(), sysService.getTemplateType(), id);
        File rootDirectory = new File(rootPath);
        FileUtils.deleteDirectory(rootDirectory);

        if (sysService.getStatus() != null) {
            // 发送MQTT，删除系统服务
            String topic = sysService.getIntegrationId() + "/" + sysService.getRunNodeId() + "/" + sysService.getTemplateType() + "/" + sysService.getId() + "/" + OperationType.DELETE.getName();
            String operationId = operationRecordService.insert(sysService, OperationType.DELETE);
            mqttClient.publish(topic, sysService.getId(), "", "", operationId);
        }

        //删除数据
        sysServiceService.deleteById(id);

        // 删除mqtt用户和权限
        List<String> userList = new ArrayList<>();
        userList.add(id);
        emqBaseService.deletePermissionAndUser(userList);
    }


    /**
     * 判断系统服务是否在线
     *
     * @param params 参数信息
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
     * 系统服务安装
     *
     * @param params 参数信息
     */
    public void install(JSONObject params) throws Exception {
        String appid = params.getString("appid");
        SysService sysService = sysServiceService.findById(appid);

        // 上传文件
        String rootPath = Constants.getServiceRootPath(sysService.getIntegrationId(), sysService.getTemplateType(), appid);
        String downloadPath = Constants.getDownloadPathByType(sysService.getTemplateType());
        String fileName = appid + "_" + System.currentTimeMillis() + ".zip";

        File rootPathFile = new File(rootPath);
        List<String> paths = Arrays.stream(rootPathFile.listFiles()).map(tempFile -> tempFile.getPath()).collect(Collectors.toList());
        compressService.zipFiles(paths, downloadPath + fileName);

        String url = fileServerUri + "downloadWebapiInstallPackage/" + fileName;
        // 发送MQTT，系统服务
        String topic = sysService.getIntegrationId() + "/" + sysService.getRunNodeId() + "/" + sysService.getTemplateType() + "/" + sysService.getId() + "/" + OperationType.INSTALL.getName();

        String operationId = operationRecordService.insert(sysService, OperationType.INSTALL);
        mqttClient.publish(topic, sysService.getId(), url, "", operationId);

        sysService.setStatus(ServiceStatus.INSTALLING.getName());
        sysServiceService.insert(sysService);
    }

    /**
     * 根据系统服务id，获取当前版本号/远程版本号/状态
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
            throw new ValidException("版本号不存在");
        }
        String remoteVersion = sysService.getVersion().split(",")[0];

        coreSysServiceServiceInfoVm.setCurrentVersion(currentVersion);
        coreSysServiceServiceInfoVm.setRemoteVersion(remoteVersion);
        coreSysServiceServiceInfoVm.setStatus(sysService.getStatus());

        return coreSysServiceServiceInfoVm;
    }

    /**
     * 代码备份
     *
     * @param params 系统服务appid
     * @throws Exception 抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    public void codeBackup(JSONObject params,HttpServletRequest request) throws Exception {
        String appid = params.getString("appid");
        SysService sysService = sysServiceService.findById(appid);

        // 上传文件
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
//        添加到备份管理中
        backupService.insert(request,backUpParam);

    }

    /**
     * 脚本运行服务远程更新配置文件
     *
     * @param params 系统服务appid
     * @throws Exception 抛出异常
     */
    public void upgradeConfig(JSONObject params) throws Exception {
        String appid = params.getString("appid");
        SysService sysService = sysServiceService.findById(appid);

        Boolean isInstall = sysServiceService.isInstall(appid);
        if (!isInstall) {
            throw new ValidException("系统服务未安装，不允许远程更新");
        }

        if (!ServiceStatus.ONLINE.getName().equals(sysService.getStatus())) {
            throw new ValidException("系统服务离线，不允许远程更新");
        }

        // 上传文件
        File configRootPath = new File(Constants.getSysServiceConfigPath(sysService.getIntegrationId(), sysService.getTemplateType(), sysService.getId()));

        long version = System.currentTimeMillis();
        // 更新当前config版本号
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
     * 脚本运行服务远程更新脚本
     *
     * @param appid 系统服务appid
     * @throws Exception 抛出异常
     */
    public void upgradeScript(String appid) throws Exception {
        SysService sysService = sysServiceService.findById(appid);

        Boolean isInstall = sysServiceService.isInstall(appid);
        if (!isInstall) {
            throw new Exception("系统服务未安装，不允许远程更新");
        }

        if (!ServiceStatus.ONLINE.getName().equals(sysService.getStatus())) {
            throw new Exception("系统服务离线，不允许远程更新");
        }

        // 上传文件
        File scriptRootPath = new File(Constants.getSysServiceScriptPath(sysService.getIntegrationId(), sysService.getTemplateType(), sysService.getId()));

        long version = System.currentTimeMillis();
        // 更新当前script版本号
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
