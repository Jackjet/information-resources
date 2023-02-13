package d1.project.dcrun.center.webapi.common.service.mqtt.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author chengh
 */
@ApiModel(value = "MqttMessageBodyPublishScriptVm", description = "控制中心发送升级应用Script脚本文件MQTT消息体")
public class MqttMessageBodyPublishScriptVm {
    @ApiModelProperty(value = "应用Script脚本文件(zip)url")
    private String url;
    @ApiModelProperty(value = "版本号")
    private String version;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
