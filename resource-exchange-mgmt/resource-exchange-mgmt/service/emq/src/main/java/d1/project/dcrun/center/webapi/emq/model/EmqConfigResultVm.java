package d1.project.dcrun.center.webapi.emq.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "EmqConfigResultVm", description = "查询数据库配置")
public class EmqConfigResultVm {
    @ApiModelProperty(value = "数据库配置key")
    private String key;
    @ApiModelProperty(value = "数据库配置value")
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
