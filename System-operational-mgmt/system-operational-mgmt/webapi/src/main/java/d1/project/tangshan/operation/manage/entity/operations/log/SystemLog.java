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
@Table(name = "d1_system_log")
@ApiModel(value = "SystemLog", description = "系统日志")
public class SystemLog extends BaseCreateEntity {
    @ApiModelProperty("事件源")
    private String source;
    @ApiModelProperty("事件信息")
    private String info;
    @ApiModelProperty("事件结果 0失败1成功")
    private String result;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
