package d1.project.dcrun.center.webapi.common.service.mqtt.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author chengh
 */
@ApiModel(value = "MqttMessageBodyUpgradeAppVm", description = "应用打包管理中远程升级消息体")
public class MqttMessageBodyUpgradeAppVm {
    @ApiModelProperty(value = "微应用升级文件(zip)的url。(即打包记录中resultUrl字段值)")
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
