package d1.project.d1project.business.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "d1_rule_template")
@ApiModel(value = "RuleTemplate", description = "规则模板")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class RuleTemplate extends BaseCreateAndUpdateEntity {

    @ApiModelProperty("模板名称")
    private String name;

    @ApiModelProperty("模板类型：0内置、1自定义")
    private int type;

    @ApiModelProperty("模板描述")
    @Column(columnDefinition = "TEXT")
    private String description;

    @ApiModelProperty("模板状态：0禁用、1启用")
    private int status;

    @ApiModelProperty("模板编码")
    private String code;

    @ApiModelProperty("引用次数")
    private int useCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public int getUseCount() {
        return useCount;
    }

    public void setUseCount(int useCount) {
        this.useCount = useCount;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
