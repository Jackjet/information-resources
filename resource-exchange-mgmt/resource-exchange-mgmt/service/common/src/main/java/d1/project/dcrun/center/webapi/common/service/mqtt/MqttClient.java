package d1.project.dcrun.center.webapi.common.service.mqtt;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.project.dcrun.center.webapi.common.service.mqtt.model.MqttMessageBodyUpgradeAppVm;
import d1.project.dcrun.center.webapi.common.service.mqtt.model.MqttMessageHeaderVm;
import d1.project.dcrun.center.webapi.common.service.mqtt.model.MqttMessageVm;
import d1.project.dcrun.center.webapi.common.service.system.SysService;
import d1.project.dcrun.center.webapi.common.service.system.SysServiceDao;
import net.dcrun.component.mqtt.client.IMqttClientService;
import net.dcrun.component.security.HmacSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 服务启动成功后，订阅MQTT主题类
 *
 * @Author chengh
 **/
@Component
public class MqttClient {
    @Autowired
    IMqttClientService mqttClientService;
    @Autowired
    SysServiceDao sysServiceDao;

    /**
     * 发送MQTT
     *
     * @param topic       主题
     * @param appid       系统服务appid
     * @param downloadUrl 下载url
     * @param version     版本号
     * @throws Exception 异常
     */
    public void publish(String topic, String appid, String downloadUrl, String version, String operationId) throws Exception {
        MqttMessageHeaderVm header = new MqttMessageHeaderVm();
        header.setAppid(appid);
        long timestamp = System.currentTimeMillis();
        header.setTimestamp(String.valueOf(System.currentTimeMillis()));

        SysService sysService = sysServiceDao.findById(appid).orElseThrow(() -> new ValidException("该系统服务不存在"));
        header.setSign(new HmacSignService().sign(appid, String.valueOf(timestamp), sysService.getAppkey()));

        // MQTT消息体
        MqttMessageBodyUpgradeAppVm body = new MqttMessageBodyUpgradeAppVm();
        body.setUrl(downloadUrl);
        body.setVersion(version);

        MqttMessageVm<MqttMessageBodyUpgradeAppVm> message = new MqttMessageVm<>();
        message.setId(operationId);
        message.setHeader(header);
        message.setBody(body);
        mqttClientService.fire(topic, JSONObject.toJSON(message).toString());
    }
}
