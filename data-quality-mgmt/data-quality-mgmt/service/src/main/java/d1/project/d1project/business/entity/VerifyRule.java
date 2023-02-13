package d1.project.d1project.business.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "d1_verify_rule")
@ApiModel(value = "VerifyRule", description = "质量检查规则")
public class VerifyRule extends BaseCreateAndUpdateEntity {

    @ApiModelProperty("规则名称")
    private String name;

    @ApiModelProperty("规则模板id")
    private String ruleTemplateId;

    @ApiModelProperty("规则模板名称")
    private String ruleTemplateName;

    @ApiModelProperty("规则描述")
    @Column(columnDefinition = "TEXT")
    private String description;

    @ApiModelProperty("规则状态：0失效、1生效")
    private int status;

    @ApiModelProperty("元数据：json格式表示")
    @Column(columnDefinition = "TEXT")
    private String metadataData;

    @ApiModelProperty("生成的sql语句")
    @Column(columnDefinition = "TEXT")
    private String sql;

    @ApiModelProperty("创建人名称")
    private String createByName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRuleTemplateId() {
        return ruleTemplateId;
    }

    public void setRuleTemplateId(String ruleTemplateId) {
        this.ruleTemplateId = ruleTemplateId;
    }

    public String getRuleTemplateName() {
        return ruleTemplateName;
    }

    public void setRuleTemplateName(String ruleTemplateName) {
        this.ruleTemplateName = ruleTemplateName;
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

    public String getMetadataData() {
        return metadataData;
    }

    public void setMetadataData(String metadataData) {
        this.metadataData = metadataData;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }
}
