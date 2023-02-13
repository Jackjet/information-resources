package d1.project.dcrun.center.webapi.common.service.mqtt.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author libin
 */
@ApiModel(value = "MqttMessageHeaderVm", description = "MqttMessageHeaderVm")
public class MqttMessageHeaderVm {
    @ApiModelProperty(value = "appid")
    private String appid;
    @ApiModelProperty(value = "时间戳")
    private String timestamp;
    @ApiModelProperty(value = "签名")
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
