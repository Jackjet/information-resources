package d1.project.resourcesharingmgmt.system.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "d1_system_settings")
@ApiModel(value = "SystemSettingsEntity", description = "系统设置")
public class SystemSettingsEntity extends BaseCreateAndUpdateEntity {

    @ApiModelProperty("类型")
    @Column(length = 20)
    private String type;

    @ApiModelProperty("值")
    @Column(columnDefinition = "TEXT")
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
