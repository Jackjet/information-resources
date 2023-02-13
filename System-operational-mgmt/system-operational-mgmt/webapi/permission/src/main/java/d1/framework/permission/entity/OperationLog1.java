package d1.framework.permission.entity;

import d1.framework.webapi.entity.BaseCreateEntity;
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
@Table(name = "d1_operation_log")
@ApiModel(value = "OperationLog", description = "操作日志")
public class OperationLog1 extends BaseCreateEntity {
    @ApiModelProperty("事件类型")
    private String type;
    @ApiModelProperty("事件源")
    private String source;
    @ApiModelProperty("操作对象")
    private String operand;
    @ApiModelProperty("操作结果 0失败1成功")
    private String result;
    @ApiModelProperty("详细描述")
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
    @ApiModelProperty("操作员")
    private String operator;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
