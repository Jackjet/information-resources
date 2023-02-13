package d1.project.tangshan.operation.manage.entity.database;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author lin
 */
@Entity
@Table(name = "d1_parameter_config")
@ApiModel(value = "ParameterConfig", description = "参数配置")
public class ParameterConfig extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("配置内容")
    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    @ApiModelProperty("备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
