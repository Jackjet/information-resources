package d1.project.dcrun.center.webapi.system.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.TokenService;
import d1.project.dcrun.center.webapi.common.service.emq.EmqBaseService;
import d1.project.dcrun.center.webapi.common.service.keydict.KeyDictService;
import d1.project.dcrun.center.webapi.common.service.management.config.ManagementConfig;
import d1.project.dcrun.center.webapi.common.service.management.config.ManagementConfigService;
import d1.project.dcrun.center.webapi.common.service.mqtt.model.MqttMessageBodyUpgradeAppVm;
import d1.project.dcrun.center.webapi.common.service.mqtt.model.MqttMessageHeaderVm;
import d1.project.dcrun.center.webapi.common.service.mqtt.model.MqttMessageVm;
import d1.project.dcrun.center.webapi.common.service.system.SysService;
import d1.project.dcrun.center.webapi.common.service.system.SysServiceDao;
import d1.project.dcrun.center.webapi.common.util.*;
import d1.project.dcrun.center.webapi.system.dao.RunNodeDao;
import d1.project.dcrun.center.webapi.system.entity.RunNode;
import d1.project.dcrun.center.webapi.system.entity.SysInstallPack;
import d1.project.dcrun.center.webapi.system.model.RunNodeTreeJsonVm;
import d1.project.dcrun.center.webapi.system.model.runnode.RunNodeVm;
import net.dcrun.component.compression.ICompressService;
import net.dcrun.component.mqtt.client.IMqttClientService;
import net.dcrun.component.security.IHmacSignService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author libin
 */
@Service
public class RunNodeService {
    private static Logger logger = LoggerFactory.getLogger(RunNodeService.class);

    @Value("${file.server.uri}")
    private String fileDownload;

    @Autowired
    IMqttClientService mqttClientService;
    @Autowired
    private RunNodeDao runNodeDao;
    @Autowired
    private SysServiceDao sysServiceDao;
    @Autowired
    private ICompressService compressService;
    @Autowired
    private ManagementConfigService managementConfigService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IHmacSignService hmacSignService;

    @Autowired
    private EmqBaseService emqBaseService;

    @Autowired
    private KeyDictService keyDictService;

    @Autowired
    private SysInstallPackService sysInstallPackService;

    /**
     * ??????????????????id??????????????????
     *
     * @return ????????????
     * @throws Exception ????????????????????????????????????
     */
    public List<RunNodeTreeJsonVm> findAllByIntegrationIdAndPath(JSONObject params) throws Exception {
        String integrationId = params.getString("integrationId");
        String name = params.getString("name");
        if (StringUtils.isEmpty(integrationId)) {
            throw new ValidException("????????????id????????????");
        }

        List<RunNode> runNodes = this.runNodeDao.findAllByIntegrationIdAndParentNodeIdOrderByCreateTimeDesc(integrationId, "");
        List<RunNodeTreeJsonVm> result = new ArrayList<>();
        if (runNodes == null) {
            return result;
        }

        Integer id = 1;
        for (RunNode runNode : runNodes) {
            RunNodeTreeJsonVm runNodeTreeJsonVm = getRootNode(runNode, id, name);
            if (runNodeTreeJsonVm == null) {
                continue;
            }
            //????????????
            result.add(runNodeTreeJsonVm);
            id++;
        }

        return result;
    }

    /**
     * ???????????????
     *
     * @param runNode ????????????
     * @return ????????????
     */
    private RunNodeTreeJsonVm getRootNode(RunNode runNode, Integer id, String name) {
        RunNodeTreeJsonVm node = new RunNodeTreeJsonVm();
        BeanUtils.copyProperties(runNode, node);

        node.setNodeId(runNode.getId());
        node.setId(id);

        if (runNode.getReportTime() == null) {
            node.setStatus("0");
        } else {
            node.setStatus(MyUtils.isOnline(runNode.getReportTime().getTime()) ? "1" : "0");
        }

        List<RunNodeTreeJsonVm> children = this.getChildNodes(node.getNodeId(), node.getId(), name);
        //????????????????????????????????????????????????????????? name
        if (children.size() == 0 && !runNode.getName().contains(name)) {
            return null;
        }
        node.setChildren(children);
        return node;
    }

    @Transactional(rollbackFor = Exception.class)
    public RunNodeVm getById(String id) throws Exception {
        RunNode runNode = runNodeDao.findById(id).orElse(null);
        if (runNode == null) {
            throw new ValidException("?????????????????????");
        }

        RunNodeVm runNodeVm = new RunNodeVm();
        BeanUtils.copyProperties(runNode, runNodeVm);

        if (runNode.getReportTime() == null || !MyUtils.isOnline(runNode.getReportTime().getTime())) {
            runNodeVm.setStatus("0");
            handleSysServiceStatus(runNode.getId());
            return runNodeVm;
        }

        runNodeVm.setStatus("1");
        return runNodeVm;
    }

    private void handleSysServiceStatus(String runNodeId) {
        List<SysService> sysServices = sysServiceDao.findAllByRunNodeId(runNodeId);
        for (SysService sysService : sysServices) {
            if (sysService.getStatus() != null) {
                sysService.setStatus(ServiceStatus.OFFLINE.getName());
            }
        }
        sysServiceDao.saveAll(sysServices);
    }

    /**
     * ????????????
     *
     * @param request
     * @param params  ????????????
     * @throws Exception ??????????????????????????????
     */
    public void insert(HttpServletRequest request, JSONObject params) throws Exception {
        String integrationId = params.getString("integrationId");

        String name = params.getString("name");
        String parentNodeId = params.getString("parentNodeId");

        String remark = params.getString("remark");

        //??????????????????url???????????????url????????????????????????
        ManagementConfig url = managementConfigService.findByConfigKey("manage.center.url");
        if (url == null || StringUtils.isEmpty(url.getConfigValue())) {
            throw new ValidException("??????????????????????????????");
        }
        ManagementConfig mqUrl = managementConfigService.findByConfigKey("manage.center.mq.url");
        if (mqUrl == null || StringUtils.isEmpty(mqUrl.getConfigValue())) {
            throw new ValidException("????????????????????????");
        }

        RunNode childNode = this.runNodeDao.findFirstByNameAndParentNodeIdAndIntegrationId(name, parentNodeId, integrationId);
        if (childNode != null) {
            throw new ValidException("????????????????????????");
        }

        childNode = new RunNode();
        childNode.setId(keyDictService.generateAppId());
        childNode.setAppkey(MyUtils.generatePrimaryKeyId());
        childNode.setIntegrationId(integrationId);
        childNode.setName(name);
        childNode.setParentNodeId(parentNodeId);
        childNode.setRemark(remark);
        childNode.setPath(this.generatePath(parentNodeId));

        String installUrl = createRunNodeFile(request, childNode.getId(), childNode.getAppkey(), childNode.getIntegrationId());
        String[] urls = installUrl.split("&&");
        childNode.setInstallUrl(urls[0]);
        childNode.setInstallBatUrl(urls[1]);
        tokenService.updateCreateIdAndTime(request, childNode);
        runNodeDao.save(childNode);

        //??????emq
        emqUserAndPermission(childNode.getId(), childNode.getAppkey(), childNode.getIntegrationId());
    }

    /**
     * ??????????????????????????????????????????????????????install.sh??????
     *
     * @param id            ??????id
     * @param integrationId ??????id
     * @return install.sh??????????????????
     * @throws Exception
     */
    public String createRunNodeFile(HttpServletRequest request, String id, String key, String integrationId) throws Exception {

        String rootPath = Constants.getServiceRootPath(integrationId, ServiceType.NODE.getName(), id);
        File rootPathFile = new File(rootPath);
        rootPathFile.mkdirs();


        //??????????????????????????????
        List<SysInstallPack> versions = sysInstallPackService.findAllVersion(request, ServiceType.NODE.getName());
        if (versions == null || versions.size() == 0) {
            throw new Exception("???????????????????????????????????????");
        }
        unzipFile(versions.get(0).getFilename(), rootPath);

        //??????config?????????application.properties????????????,??????jar?????????
        String nodeApplicationPath = Constants.getSysServiceConfigPath(integrationId, ServiceType.NODE.getName(), id) + "application.properties";
        Properties p = MyPropertiesUtil.init(nodeApplicationPath);
        MyPropertiesUtil.update("manage.center.url", managementConfigService.findByConfigKey("manage.center.url").getConfigValue(), nodeApplicationPath, p);
        MyPropertiesUtil.update("manage.center.mq.url", managementConfigService.findByConfigKey("manage.center.mq.url").getConfigValue(), nodeApplicationPath, p);
        MyPropertiesUtil.update("manage.center.file.url", managementConfigService.findByConfigKey("manage.center.url").getConfigValue(), nodeApplicationPath, p);
        MyPropertiesUtil.update("appid", id, nodeApplicationPath, p);
        MyPropertiesUtil.update("appkey", key, nodeApplicationPath, p);
        MyPropertiesUtil.update("integrationId", integrationId, nodeApplicationPath, p);

        // ??????jar?????????
        String jarName = "";
        for (File tempFile : new File(rootPath).listFiles()) {
            if (tempFile.isFile() && tempFile.getName().endsWith(".jar")) {
                jarName = "dcrun-" + id + "-" + tempFile.getName();
                tempFile.renameTo(new File(rootPath + jarName));
            }
        }

        if (StringUtils.isEmpty(jarName)) {
            throw new ValidException("??????jar???????????????");
        }

        createRunSh(rootPath, id, jarName);
        createRunBat(rootPath, id, jarName);

        String fileName = id + "_" + System.currentTimeMillis();

        //???????????????files/storage/node/??????id_.zip
        File outputZipFile = new File(Constants.getDownloadPathByType(ServiceType.NODE.getName()), fileName + ".zip");

        List<String> paths = Arrays.stream(rootPathFile.listFiles()).map(tempFile -> tempFile.getPath()).collect(Collectors.toList());
        compressService.zipFiles(paths, outputZipFile.getAbsolutePath());

        String nodeZipUrl = fileDownload + "downloadNode/" + outputZipFile.getName();

        // ??????install.sh??????
        File targetFile = new File(outputZipFile.getParent(), fileName + ".sh");

        ClassPathResource classPathResource = new ClassPathResource(Constants.TEMPLATE_NODE_INSTALL);
        FileUtils.copyInputStreamToFile(classPathResource.getInputStream(), targetFile);

        // ??????install.sh????????????
        String text = FileUtils.readFileToString(targetFile, StandardCharsets.UTF_8);
        text = text.replace("{##appid##}", id);
        text = text.replace("{##URL##}", nodeZipUrl);
        text = text.replace("{##fileName##}", fileName);
        FileUtils.write(targetFile, text, StandardCharsets.UTF_8);

        // ??????install.sh???????????? ./files/storage/??????appid/
        return fileDownload + "downloadNode/" + targetFile.getName() + "&&" + nodeZipUrl;
    }

    /**
     * ????????????run.sh??????
     *
     * @param rootPath
     * @param id
     * @param jarName
     * @throws Exception
     */
    private void createRunSh(String rootPath, String id, String jarName) throws Exception {
        //??????resources/node/run.sh?????????files/services/nodes??????????????????id(??????)appid?????????
        String runPath = rootPath + "run.sh";
        File runFile = new File(runPath);

        // ????????????
        ClassPathResource classPathResource = new ClassPathResource(Constants.TEMPLATE_NODE_RUNSH);
        FileUtils.copyInputStreamToFile(classPathResource.getInputStream(), runFile);

        //?????????
        JSONObject paramsh = new JSONObject();
        paramsh.put("{##appid##}", id);
        paramsh.put("{##jar##}", jarName.split(".jar")[0]);
        paramsh.put("{##jar.jar##}", jarName);
        MyUtils.replaceForTemplate(runFile, paramsh);
    }

    /**
     * ????????????run.bat??????
     *
     * @param rootPath
     * @param id
     * @param jarName
     * @throws Exception
     */
    private void createRunBat(String rootPath, String id, String jarName) throws Exception {
        //??????resources/node/run.bat?????????files/services/nodes??????????????????id(??????)appid?????????
        String runBatPath = rootPath + "run.bat";
        File runBatFile = new File(runBatPath);

        // ????????????
        ClassPathResource classPathResource = new ClassPathResource(Constants.TEMPLATE_NODE_RUNBAT);
        FileUtils.copyInputStreamToFile(classPathResource.getInputStream(), runBatFile);

        //?????????
        JSONObject parambatsh = new JSONObject();
        parambatsh.put("{##jar.jar##}", jarName);
        MyUtils.replaceForTemplate(runBatFile, parambatsh);
    }

    /**
     * ???????????????
     *
     * @param filename ??????????????????
     * @param rootPath ?????????
     * @throws Exception
     */
    private void unzipFile(String filename, String rootPath) throws Exception {
        File outputFile = new File(Constants.FILES_BASIC_NODE_INSTALL, filename);
        if (!outputFile.exists()) {
            logger.error("????????????????????????????????????", outputFile.getAbsoluteFile());
            throw new Exception("?????????????????????");
        }
        compressService.unzip(outputFile.getAbsolutePath(), rootPath);
    }

    /**
     * ??????path
     *
     * @param parentNodeId ?????????id
     * @return path
     */
    private String generatePath(String parentNodeId) throws ValidException {
        //?????????????????????
        if (StringUtils.isEmpty(parentNodeId)) {
            return "0000";
        }
        //???????????????
        RunNode parentNode = this.runNodeDao.findById(parentNodeId).orElseThrow(() -> new ValidException("??????????????????"));

        List<RunNode> childs = this.runNodeDao.findAllByParentNodeIdOrderByCreateTimeDesc(parentNode.getId());
        Format format = new DecimalFormat("0000");

        if (childs != null && childs.size() > 0) {
            return parentNode.getPath() + format.format(childs.size());
        } else {
            return parentNode.getPath() + format.format(0);
        }
    }

    /**
     * ????????????appid(id)???appkey???????????????emq???????????????????????????????????????appid/sys/????????????appid/+??????????????????
     *
     * @param appid         appid
     * @param appkey        appkey
     * @param integrationId integrationId
     * @throws Exception Exception
     */
    public void emqUserAndPermission(String appid, String appkey, String integrationId) throws Exception {
        //??????????????????
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> mapUser = new HashMap<>(2);
        mapUser.put("userName", appid);
        mapUser.put("password", appkey);
        list.add(mapUser);

        //??????
        String topic = integrationId + "/#";
        List<Map<String, String>> listTopic = new ArrayList<>();
        Map<String, String> mapTopic = new HashMap<>(3);
        mapTopic.put("userName", appid);
        mapTopic.put("access", "2");
        mapTopic.put("topic", topic);
        listTopic.add(mapTopic);

        emqBaseService.addUserAndPermission(list, listTopic);
    }

    /**
     * ????????????
     *
     * @param request
     * @param params  ????????????
     * @throws Exception ??????????????????????????????
     */
    public void update(HttpServletRequest request, JSONObject params) throws Exception {
        String id = params.getString("id");
        String remark = params.getString("remark");

        RunNode runNode = this.runNodeDao.findById(id).orElseThrow(() -> new ValidException("???????????????"));
        runNode.setRemark(remark);
        tokenService.updateUpdateIdAndTime(request, runNode);
        runNodeDao.save(runNode);
    }

    /**
     * ????????????
     *
     * @param id ????????????
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws Exception {
        RunNode node = runNodeDao.findById(id).orElse(null);
        if (node == null) {
            throw new ValidException("???????????????");
        }

        List<RunNode> runNodes = runNodeDao.findAllByParentNodeIdOrderByCreateTimeDesc(node.getId());
        if (runNodes.size() > 0) {
            throw new ValidException("??????????????????????????????????????????");
        }

        List<SysService> sysServices = sysServiceDao.findAllByRunNodeId(node.getId());
        if (sysServices.size() > 0) {
            throw new ValidException("???????????????????????????????????????????????????");
        }

        //??????????????????
        String rootPath = Constants.getServiceRootPath(node.getIntegrationId(), ServiceType.NODE.getName(), id);
        FileUtils.deleteDirectory(new File(rootPath));

        if (node.getReportTime() != null && MyUtils.isOnline(node.getReportTime().getTime())) {
            //??????????????????: ????????????id/??????ID/????????????/??????ID/delete
            String topic = node.getIntegrationId() + "/" + node.getId() + "/" + ServiceType.NODE.getName() + "/" + node.getId() + "/" + OperationType.DELETE.getName();
            publishDeleteNode(topic, node.getId());
        }

        // ??????
        runNodeDao.delete(node);

        // ??????mqtt???????????????
        List<String> userList = new ArrayList<>();
        userList.add(id);
        emqBaseService.deletePermissionAndUser(userList);

    }

    /**
     * ??????????????????????????????????????????????????????????????????
     *
     * @param parentId ?????????
     * @return ?????????????????????
     */
    private List<RunNodeTreeJsonVm> getChildNodes(String parentId, Integer id, String name) {
        List<RunNode> childs = this.runNodeDao.findAllByParentNodeIdOrderByCreateTimeDesc(parentId);

        //????????????????????????????????????????????????????????????
        if (childs != null && childs.size() > 0) {
            List<RunNodeTreeJsonVm> nodes = new ArrayList<>();
            Integer count = 1;
            id = id * 10;
            for (RunNode runNode : childs) {
                RunNodeTreeJsonVm node = new RunNodeTreeJsonVm();
                BeanUtils.copyProperties(runNode, node);
                node.setNodeId(runNode.getId());
                node.setId(id + count);

                if (runNode.getReportTime() == null) {
                    node.setStatus("0");
                } else {
                    node.setStatus(MyUtils.isOnline(runNode.getReportTime().getTime()) ? "1" : "0");
                }

                List<RunNodeTreeJsonVm> children = this.getChildNodes(node.getNodeId(), node.getId(), name);
                //????????????????????????????????????????????????????????? name
                if (children.size() == 0 && !runNode.getName().contains(name)) {
                    continue;
                }
                node.setChildren(children);
                nodes.add(node);
                count++;
            }
            return nodes;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * ????????????MQTT???????????????????????????????????????
     *
     * @param model ?????????????????????
     */
    public void ping(String model) {
        JSONObject jsonStr = JSONObject.parseObject(model);
        JSONObject head = jsonStr.getJSONObject("header");
        String id = head.getString("appid");

        RunNode runNode = runNodeDao.findById(id).orElse(null);
        if (runNode == null) {
            logger.info("???????????????");
        } else {
            runNode.setReportTime(new Date());
            runNodeDao.save(runNode);

            JSONObject body = jsonStr.getJSONObject("body");
            for (Map.Entry<String, Object> entry : body.entrySet()) {
                String appid = entry.getKey();
                String value = entry.getValue().toString();

                Optional<SysService> optionalSysService = sysServiceDao.findById(appid);
                if (!optionalSysService.isPresent()) {
                    logger.info("?????????????????????" + appid);
                    continue;
                }
                SysService sysService = optionalSysService.get();
                JSONObject jValue = JSON.parseObject(value);
                String version = jValue.getString("version");
                String status = jValue.getString("status");
                if ("1".equals(status)) {
                    status = ServiceStatus.ONLINE.getName();
                } else {
                    status = ServiceStatus.OFFLINE.getName();
                }

                sysService.setVersion(version);
                sysService.setStatus(status);

                sysServiceDao.save(sysService);
            }
        }
    }

    /**
     * ????????????????????????????????????
     *
     * @param params ????????????
     */
    public boolean isOnline(JSONObject params) {
        RunNode runNode = runNodeDao.findById(params.getString("id")).orElse(null);
        if (runNode == null || runNode.getReportTime() == null) {
            return false;
        }
        return MyUtils.isOnline(runNode.getReportTime().getTime());
    }

    /**
     * ??????????????????MQTT
     *
     * @param topic ??????
     * @param appid ??????????????????appid
     * @throws Exception ??????
     */
    public void publishDeleteNode(String topic, String appid) throws Exception {
        MqttMessageHeaderVm header = new MqttMessageHeaderVm();
        header.setAppid(appid);
        long timestamp = System.currentTimeMillis();
        header.setTimestamp(String.valueOf(System.currentTimeMillis()));

        RunNode runNode = runNodeDao.findById(appid).orElse(null);
        if (runNode == null) {
            throw new ValidException("??????????????????????????????");
        }
        header.setSign(hmacSignService.sign(appid, String.valueOf(timestamp), runNode.getAppkey()));

        // MQTT?????????
        MqttMessageBodyUpgradeAppVm body = new MqttMessageBodyUpgradeAppVm();

        MqttMessageVm<MqttMessageBodyUpgradeAppVm> message = new MqttMessageVm<>();
        message.setId(keyDictService.generateAppId());
        message.setHeader(header);
        message.setBody(body);
        mqttClientService.fire(topic, JSONObject.toJSON(message).toString());
    }

//    /**
//     * ??????????????????
//     * @return
//     * @throws Exception
//     */
//    private TemplateVm getTemplate() throws Exception {
//        Map<String, Object> findParams = new HashMap<>();
//        findParams.put("type", ServiceType.NODE.getName());
//        String result = developerCenterService.getInstallPackage(ServiceType.NODE.getName());
//                //httpService.get(developerCenterUrl + "service/install/package/find/", null, findParams);
//        JSONObject resultJson = (JSONObject) JSONObject.parse(result);
//        TemplateVm template = JSONObject.toJavaObject((JSONObject) resultJson.get("data"), TemplateVm.class);
//
//        if (template == null) {
//            throw new ValidException("????????????????????????");
//        }
//
//        return template;
//    }

    public RunNode findById(String id) {
        return runNodeDao.findById(id).orElse(null);
    }


    public String getRunNodeName(String id) {
        Optional<RunNode> runNode = runNodeDao.findById(id);
        if (runNode.isPresent() && runNode.get() != null) {
            return runNode.get().getName();
        }
        return null;
    }

}
