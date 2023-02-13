package d1.project.dcrun.center.webapi.common.service.mqtt.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author libin
 */
@ApiModel(value = "MqttMessageVm", description = "mqtt内容")
public class MqttMessageVm<T> {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "header")
    private MqttMessageHeaderVm header;
    @ApiModelProperty(value = "body")
    private T body;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MqttMessageHeaderVm getHeader() {
        return header;
    }

    public void setHeader(MqttMessageHeaderVm header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
