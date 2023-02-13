package d1.project.dcrun.center.webapi.common.service.system;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.dcrun.center.webapi.common.service.mqtt.MqttClient;
import d1.project.dcrun.center.webapi.common.service.operation.record.OperationRecordService;
import d1.project.dcrun.center.webapi.common.util.OperationType;
import d1.project.dcrun.center.webapi.common.util.ServiceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * @author libin
 */
@Service
public class SysServiceService {
    private final SysServiceDao sysServiceDao;
    private final MqttClient mqttClient;
    private final OperationRecordService operationRecordService;

    @Autowired
    public SysServiceService(SysServiceDao sysServiceDao, MqttClient mqttClient, OperationRecordService operationRecordService) {
        this.sysServiceDao = sysServiceDao;
        this.mqttClient = mqttClient;
        this.operationRecordService = operationRecordService;
    }

    public Page<Map> selectVersion(String integrationId) throws Exception{

        JSONObject params = new JSONObject();
        params.put("page","1");
        params.put("size","1000");
        SpecificationBuilder<SysService> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params).build();

        return this.sysServiceDao.selectVersion(integrationId,"cmd",builder.getPageable());
    }

    /**
     * 系统服务名称是否存在
     *
     * @param name      系统服务名称
     * @param runNodeId 运行节点id
     * @return 返回结果
     */
    public Boolean existsByNameAndRunNodeId(String name, String runNodeId) {
        return sysServiceDao.existsByNameAndRunNodeId(name, runNodeId);
    }

    public Boolean existsTypeVersion(String integrationId, String typeName,String version) {
        return sysServiceDao.getTypeVersionCount(integrationId,typeName,version)>0;
    }

    public Page<SysService> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<SysService> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params)
                .sEqual("integrationId", "integrationId")
                .sEqual("runNodeId", "runNodeId")
                .sContain("name", "name")
                .sEqual("templateType", "templateType")
                .dOrder("createTime").build();

        return sysServiceDao.findAll(specification, builder.getPageable());
    }

    public void deleteById(String id) {
        sysServiceDao.deleteById(id);
    }

    /**
     * 启动系统服务
     *
     * @param
     * @throws Exception 抛出异常
     */
    public void start(String appid) throws Exception {

        Boolean isInstall = isInstall(appid);
        if (!isInstall) {
            throw new ValidException("系统服务未安装，不允许启动操作");
        }


        SysService sysService = findById(appid);
        // 发送MQTT，启动系统服务
        String topic = sysService.getIntegrationId() + "/" + sysService.getRunNodeId() + "/" + sysService.getTemplateType() + "/" + sysService.getId() + "/" + OperationType.START.getName();
        String operationId = operationRecordService.insert(sysService, OperationType.START);
        mqttClient.publish(topic, sysService.getId(), "", "", operationId);

        //修改服务启动状态
        sysService.setStatus(ServiceStatus.ONLINE.getName());
        sysServiceDao.save(sysService);
    }

    /**
     * 停止系统服务
     *
     * @param appid 系统appid
     * @throws Exception 抛出异常
     */
    public void stop(String appid) throws Exception {
        Boolean isInstall = isInstall(appid);
        if (!isInstall) {
            throw new ValidException("系统服务未安装，不允许停止操作");
        }
        SysService sysService = findById(appid);

        // 发送MQTT，启动系统服务
        String topic = sysService.getIntegrationId() + "/" + sysService.getRunNodeId() + "/" + sysService.getTemplateType() + "/" + sysService.getId() + "/" + OperationType.STOP.getName();
        String operationId = operationRecordService.insert(sysService, OperationType.STOP);
        mqttClient.publish(topic, sysService.getId(), "", "", operationId);

        //修改服务启动状态
        sysService.setStatus(ServiceStatus.OFFLINE.getName());
        sysServiceDao.save(sysService);
    }

    /**
     * 判断系统服务是否安装过
     *
     * @param appid 系统appid
     */
    public boolean isInstall(String appid) {
        Optional<SysService> sysServiceOptional = sysServiceDao.findById(appid);
        if (!sysServiceOptional.isPresent()) {
            return false;
        }

        SysService sysService = sysServiceOptional.get();
        if (sysService == null) {
            return false;
        }

        return sysService.getStatus() != null;
    }

    public Integer getVersion(String integrationId,String templateType){
        return this.sysServiceDao.getVersion(integrationId,templateType);
    }

    public SysService findById(String id) throws Exception {
        Optional<SysService> sysServiceOptional = sysServiceDao.findById(id);
        return sysServiceOptional.orElseThrow(() -> new ValidException("不存在该系统服务"));
    }

    public void insert(SysService sysService) {
        sysServiceDao.save(sysService);
    }
}
