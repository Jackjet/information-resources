package d1.project.dcrun.center.webapi.datacache.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.dcrun.center.webapi.common.service.mqtt.MqttClient;
import d1.project.dcrun.center.webapi.common.service.operation.record.OperationRecordService;
import d1.project.dcrun.center.webapi.common.service.system.SysService;
import d1.project.dcrun.center.webapi.common.service.system.SysServiceService;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import d1.project.dcrun.center.webapi.common.util.OperationType;
import d1.project.dcrun.center.webapi.datacache.dao.KeyConfigDao;
import d1.project.dcrun.center.webapi.datacache.entity.KeyConfig;
import d1.project.dcrun.center.webapi.datacache.utils.DataCacheUtils;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author maoyy
 */
@Service
public class KeyConfigService {
    @Value("${file.server.uri}")
    private String fileServerUri;

    private final KeyConfigDao keyConfigDao;
    private final TokenService tokenService;
    private final SysServiceService sysServiceService;
    private final OperationRecordService operationRecordService;
    private final MqttClient mqttClient;

    @Autowired
    public KeyConfigService(KeyConfigDao keyConfigDao, TokenService tokenService, SysServiceService sysServiceService, OperationRecordService operationRecordService, MqttClient mqttClient) {
        this.keyConfigDao = keyConfigDao;
        this.tokenService = tokenService;
        this.sysServiceService = sysServiceService;
        this.operationRecordService = operationRecordService;
        this.mqttClient = mqttClient;
    }

    public List<KeyConfig> findAllBySysServiceIdAndKey(String sysServiceId, String key) throws Exception {
        return keyConfigDao.findAllBySysServiceIdAndKey(sysServiceId, key);
    }

    public void deleteBatch(List<KeyConfig> keyConfigs) throws Exception {
        keyConfigDao.deleteInBatch(keyConfigs);
    }

    /**
     * 查询列表
     *
     * @param params 请求参数
     * @return 返回值
     * @throws Exception 抛出异常
     */
    public Page<KeyConfig> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<KeyConfig> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params)
                .sEqual("integrationId", "integrationId")
                .sEqual("sysServiceId", "sysServiceId")
                .sContain("key", "key")
                .dOrder("createTime").build();

        return this.keyConfigDao.findAll(specification, builder.getPageable());
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(HttpServletRequest request, JSONObject params) throws Exception {
        String integrationId = params.get("integrationId").toString();
        String sysServiceId = params.get("sysServiceId").toString();
        String key = params.get("key").toString();
        Integer expire = Integer.parseInt(params.get("expire").toString());
        String data = params.get("data").toString();
        JSONArray json = JSONArray.parseArray(data);

        if (json.size() < 0) {
            throw new ValidException("配置信息不能为空");
        }

        for (int i = 0; i < json.size(); i++) {
            // 遍历 jsonarray 数组，把每一个对象转成 json 对象
            JSONObject job = json.getJSONObject(i);
            // 得到 每个对象中的属性值
            KeyConfig keyConfig = keyConfigDao.findBySysServiceIdAndAppidAndKey(sysServiceId, job.get("appid").toString(), key);

            if (keyConfig == null) {
                KeyConfig entity = new KeyConfig();
                entity.setId(MyUtils.generatePrimaryKeyId());
                entity.setIntegrationId(integrationId);
                entity.setSysServiceId(sysServiceId);
                entity.setKey(key);
                entity.setAppid(job.get("appid").toString());
                entity.setName(job.getString("name"));
                entity.setPermission(job.get("permission").toString());
                entity.setExpire(expire);
                tokenService.updateCreateIdAndTime(request, entity);
                keyConfigDao.save(entity);
            } else {
                keyConfig.setPermission(job.get("permission").toString());
                tokenService.updateUpdateIdAndTime(request, keyConfig);
                keyConfigDao.save(keyConfig);
            }
        }
    }

    public void deleteById(String id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            throw new ValidException("id不能为空");
        }
        KeyConfig keyConfig = keyConfigDao.findById(id).orElseThrow(() -> new Exception("找不到记录：" + id));

        keyConfigDao.delete(keyConfig);
    }

    /**
     * 同步路由数据
     *
     * @param params 集成项目id和系统服务id
     */
    public void syncKeys(JSONObject params) throws Exception {
        String integrationId = params.getString("integrationId");
        String appid = params.getString("sysServiceId");

        JSONObject jkey = new JSONObject();

        File configFile = new File(DataCacheUtils.getConfigJsonPath(integrationId, appid));
        String text = FileUtils.readFileToString(configFile, StandardCharsets.UTF_8);
        JSONObject mq = JSONObject.parseObject(text);
        mq.getString("emqUrl");
        mq.getString("user");
        mq.getString("password");
        jkey.put("mq", mq);

        List<KeyConfig> keyList = keyConfigDao.findAllByIntegrationIdAndSysServiceId(integrationId, appid);

        JSONArray permissions = new JSONArray();
        for (KeyConfig keyConfig : keyList) {
            JSONObject permission = new JSONObject();
            permission.put("appid", keyConfig.getAppid());
            permission.put("key", keyConfig.getKey());
            permission.put("expire", keyConfig.getExpire());
            permission.put("permission", keyConfig.getPermission());
            permissions.add(permission);
        }
        jkey.put("permissions", permissions);

        //创建文件
        File keyJsonFile = new File(DataCacheUtils.getKeyJsonOutputPath(appid));
        FileUtils.write(keyJsonFile, JSONObject.toJSONString(jkey), StandardCharsets.UTF_8);

        SysService sysService = sysServiceService.findById(appid);
        String operationId = operationRecordService.insert(sysService, OperationType.SYNC_KEY);

        String url = fileServerUri + "downloadDataCacheKey/" + keyJsonFile.getName();
        String topic = DataCacheUtils.getSyncKeyTopic(integrationId, sysService.getRunNodeId(), appid);
        mqttClient.publish(topic, appid, url, "", operationId);

    }
}
