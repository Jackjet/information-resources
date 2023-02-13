package d1.project.dcrun.center.webapi.common.service.management.config;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author libin
 */
@Entity
@Table(name = "d1_management_config")
@ApiModel(value = "ManagementConfig", description = "管理中心配置信息表")
public class ManagementConfig extends BaseCreateAndUpdateEntity {
    @ApiModelProperty(value = "配置名称")
    private String configKey;
    @ApiModelProperty(value = "配置值")
    private String configValue;
    @ApiModelProperty(value = "配置中文名称")
    private String configName;

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }
}
