package d1.project.tangshan.operation.manage.entity.remote;

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
@Table(name = "d1_commands")
@ApiModel(value = "Commands", description = "运维指令")
public class Commands extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("类型id")
    private String typeId;

    @ApiModelProperty("类型名称")
    private String type;

    @ApiModelProperty("指令内容")
    @Lob
    @Column(columnDefinition = "TEXT")
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    private String content;

    @ApiModelProperty("备注")
    private String remark;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
