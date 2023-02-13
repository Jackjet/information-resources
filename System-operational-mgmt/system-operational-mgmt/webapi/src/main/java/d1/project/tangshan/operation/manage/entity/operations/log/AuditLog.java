package d1.project.tangshan.operation.manage.entity.operations.log;

import d1.framework.webapi.entity.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author lin
 */
@Entity
@Table(name = "d1_audit_log")
@ApiModel(value = "AuditLog", description = "审计日志")
public class AuditLog extends BaseCreateEntity {
    @ApiModelProperty("事件类型")
    private String type;
    @ApiModelProperty("事件源")
    private String source;
    @ApiModelProperty("登录IP")
    private String loginIp;
    @ApiModelProperty("操作结果 0失败1成功")
    private String result;
    @ApiModelProperty("事件级别")
    private String level;
    @ApiModelProperty("操作对象")
    private String operand;
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

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getOperand() {
        return operand;
    }

    public void setOperand(String operand) {
        this.operand = operand;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
