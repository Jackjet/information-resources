package d1.project.dcrun.center.webapi.common.service.mqtt.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author shkstart
 * @date 2020-02-26-10:50
 */
@ApiModel(value = "MqttMessageResBodyVm", description = "mqtt内容")
public class MqttMessageResBodyVm {
    @ApiModelProperty(value = "version")
    private String version;
    @ApiModelProperty(value = "status")
    private String status;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
