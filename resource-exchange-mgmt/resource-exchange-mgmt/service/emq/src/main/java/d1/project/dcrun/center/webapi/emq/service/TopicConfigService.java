package d1.project.dcrun.center.webapi.emq.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.TokenService;
import d1.project.dcrun.center.webapi.common.service.mqtt.MqttClient;
import d1.project.dcrun.center.webapi.common.service.operation.record.OperationRecordService;
import d1.project.dcrun.center.webapi.common.service.sys.user.SysUser;
import d1.project.dcrun.center.webapi.common.service.sys.user.SysUserService;
import d1.project.dcrun.center.webapi.common.service.system.SysService;
import d1.project.dcrun.center.webapi.common.service.system.SysServiceService;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import d1.project.dcrun.center.webapi.common.util.OperationType;
import d1.project.dcrun.center.webapi.emq.dao.TopicConfigDao;
import d1.project.dcrun.center.webapi.emq.entity.TopicConfig;
import d1.project.dcrun.center.webapi.emq.entity.Topics;
import d1.project.dcrun.center.webapi.emq.model.SyncEmqUserResVm;
import d1.project.dcrun.center.webapi.emq.model.SyncTopicPermissionResVm;
import d1.project.dcrun.center.webapi.emq.model.TopicConfigFindAllResVm;
import d1.project.dcrun.center.webapi.emq.utils.EmqUtils;
import net.dcrun.component.compression.ICompressService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author xuaa
 */
@Service
public class TopicConfigService {
    @Value("${file.server.uri}")
    private String fileDownload;

    @Autowired
    ICompressService iCompressService;
    @Autowired
    private TopicConfigDao topicConfigDao;
    @Autowired
    private MqttClient mqttClient;
    @Autowired
    private TopicsService topicService;
    @Autowired
    private OperationRecordService operationRecordService;
    @Autowired
    private SysServiceService sysServiceService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private TokenService tokenService;

    /**
     * 查询列表
     *
     * @param params 请求参数
     * @return 返回值
     * @throws Exception 抛出异常
     */
    public List<TopicConfigFindAllResVm> findAll(JSONObject params) throws Exception {
        String integrationId = params.getString("integrationId");
        String sysServiceId = params.getString("sysServiceId");
        String topicId = params.getString("topicId");
        String appid = params.getString("appid");

        List<TopicConfig> topicConfigs = topicConfigDao.findAllBySysServiceIdAndTopicIdAndAppidContains(sysServiceId, topicId, appid);
        List<TopicConfigFindAllResVm> result = new ArrayList<>();

        for (TopicConfig topicConfig : topicConfigs) {
            TopicConfigFindAllResVm res = new TopicConfigFindAllResVm();
            res.setId(topicConfig.getId());
            res.setIntegrationId(integrationId);
            res.setAppid(topicConfig.getAppid());
            res.setName(topicConfig.getName());
            res.setTopicId(topicConfig.getTopicId());
            res.setPermission(topicConfig.getPermission());
            result.add(res);
        }

        return result;
    }

    /**
     * 添加
     *
     * @param request 请求
     * @param params  请求参数
     * @throws Exception 抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(HttpServletRequest request, JSONObject params) throws Exception {

        String integrationId = params.getString("integrationId");
        String appid = params.getString("sysServiceId");
        String topicId = params.getString("topicId");
        String data = params.getString("data");
        JSONArray json = JSONArray.parseArray(data);

        if (json.size() < 0) {
            throw new ValidException("配置信息不能为空");
        }

        for (int i = 0; i < json.size(); i++) {
            // 遍历 jsonarray 数组，把每一个对象转成 json 对象
            JSONObject job = json.getJSONObject(i);
            // 得到 每个对象中的属性值
            TopicConfig topicConfig = topicConfigDao.findBySysServiceIdAndAppidAndTopicId(appid, job.getString("appid"), topicId);

            if (topicConfig == null) {
                TopicConfig entity = new TopicConfig();
                entity.setIntegrationId(integrationId);
                entity.setSysServiceId(appid);
                entity.setTopicId(topicId);
                entity.setAppid(job.getString("appid"));
                entity.setName(job.getString("name"));
                entity.setPermission(job.getString("permission"));
                entity.setId(MyUtils.generatePrimaryKeyId());
                tokenService.updateCreateIdAndTime(request, entity);
                topicConfigDao.save(entity);
            } else {
                topicConfig.setPermission(job.getString("permission"));
                tokenService.updateUpdateIdAndTime(request, topicConfig);
                topicConfigDao.save(topicConfig);
            }
        }
    }

    /**
     * 同步开发者Topic访问控制权限
     *
     * @param params 请求参数
     */
    public void syncTopicPermission(JSONObject params) throws Exception {

        String integrationId = params.getString("integrationId");
        String appid = params.getString("sysServiceId");

        List<TopicConfig> topicConfigs = topicConfigDao.findAllByIntegrationIdAndSysServiceId(integrationId, appid);
        if (topicConfigs.size() == 0) {
            throw new ValidException("请先配置“访问控制”后同步");
        }

        List<SyncTopicPermissionResVm> syncTopicPermissionResVms = new ArrayList<>();
        for (TopicConfig topicConfig : topicConfigs) {
            SyncTopicPermissionResVm syncTopicPermissionResVm = new SyncTopicPermissionResVm();
            syncTopicPermissionResVm.setAppid(topicConfig.getAppid());
            //topic名称
            Topics topics = topicService.findById(topicConfig.getTopicId());
            if (topics == null) {
                continue;
            }
            syncTopicPermissionResVm.setTopicId(topics.getTopicName());
            syncTopicPermissionResVm.setPermission(topicConfig.getPermission());
            syncTopicPermissionResVms.add(syncTopicPermissionResVm);
        }

        // 生成json文件
        File permissionJsonFile = new File(EmqUtils.getPermissionJsonOutputPath(appid));
        FileUtils.write(permissionJsonFile, JSON.toJSONString(syncTopicPermissionResVms), StandardCharsets.UTF_8);

        SysService sysService = sysServiceService.findById(appid);
        //报错到数据库
        String operationId = operationRecordService.insert(sysService, OperationType.SYNC_TOPIC_PERMISSION);

        String topic = EmqUtils.getSyncPermissionTopic(integrationId, sysService.getRunNodeId(), appid);
        String url = fileDownload + "downloadEmq/" + permissionJsonFile.getName();
        mqttClient.publish(topic, appid, url, "", operationId);

        //同步开发者
        syncEmqUser(params);
    }

    public void syncEmqUser(JSONObject params) throws Exception {
        String integrationId = params.getString("integrationId");
        String appid = params.getString("sysServiceId");

        List<SysUser> sysUsers = sysUserService.findAllByIntegrationId(integrationId);
        if (sysUsers.size() == 0) {
            throw new ValidException("该系统服务下不存在开发者");
        }

        List<SyncEmqUserResVm> syncEmqUserResVms = new ArrayList<>();
        for (SysUser sysUser : sysUsers) {
            SyncEmqUserResVm syncEmqUserResVm = new SyncEmqUserResVm();
            syncEmqUserResVm.setAppid(sysUser.getAppid());
            syncEmqUserResVm.setAppkey(sysUser.getAppkey());
            syncEmqUserResVms.add(syncEmqUserResVm);
        }

        // 生成json文件
        File userJsonFile = new File(EmqUtils.getUserJsonOutputPath(appid));
        FileUtils.write(userJsonFile, JSON.toJSONString(syncEmqUserResVms), StandardCharsets.UTF_8);

        SysService sysService = sysServiceService.findById(appid);
        //保存到数据库
        String operationId = operationRecordService.insert(sysService, OperationType.SYNC_USER);

        String topic = EmqUtils.getSyncUserTopic(integrationId, sysService.getRunNodeId(), appid);
        String url = fileDownload + "downloadEmq/" + userJsonFile.getName();
        mqttClient.publish(topic, appid, url, "", operationId);
    }

    public TopicConfig findById(String id) {
        Optional<TopicConfig> topicConfigOptional = topicConfigDao.findById(id);
        return topicConfigOptional.orElse(null);
    }

    public void delete(String id) {
        topicConfigDao.deleteById(id);
    }
}
