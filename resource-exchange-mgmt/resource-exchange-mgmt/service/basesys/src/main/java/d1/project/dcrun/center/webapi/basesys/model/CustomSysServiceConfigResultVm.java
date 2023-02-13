package d1.project.dcrun.center.webapi.basesys.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author chengh
 **/
@ApiModel(value = "CustomSysServiceConfigResultVm", description = "查询自定义配置")
public class CustomSysServiceConfigResultVm {
    @ApiModelProperty(value = "自定义配置key")
    private String key;
    @ApiModelProperty(value = "自定义配置value")
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
