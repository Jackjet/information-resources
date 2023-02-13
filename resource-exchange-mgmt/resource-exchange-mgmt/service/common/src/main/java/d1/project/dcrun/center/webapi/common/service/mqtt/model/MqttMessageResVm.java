package d1.project.dcrun.center.webapi.common.service.mqtt.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author shkstart
 * @date 2020-02-26-10:49
 */
@ApiModel(value = "MqttMessageResVm", description = "mqtt内容")
public class MqttMessageResVm {
    @ApiModelProperty(value = "header")
    private MqttMessageHeaderVm header;
    @ApiModelProperty(value = "body")
    private MqttMessageResBodyVm body;

    public MqttMessageHeaderVm getHeader() {
        return header;
    }

    public void setHeader(MqttMessageHeaderVm header) {
        this.header = header;
    }

    public MqttMessageResBodyVm getBody() {
        return body;
    }

    public void setBody(MqttMessageResBodyVm body) {
        this.body = body;
    }
}
