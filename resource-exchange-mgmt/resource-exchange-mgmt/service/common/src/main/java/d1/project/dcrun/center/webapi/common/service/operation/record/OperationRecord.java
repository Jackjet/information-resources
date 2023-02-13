package d1.project.dcrun.center.webapi.common.service.operation.record;

import d1.framework.webapi.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

/**
 * @author maoyy
 */
@Entity
@Table(name = "d1_operation_record")
@ApiModel(value = "OperationRecord", description = "OperationRecord 服务操作记录")
public class OperationRecord extends BaseEntity {
    @ApiModelProperty(value = "系统服务appid")
    private String appid;
    @ApiModelProperty(value = "系统服务名称")
    private String sysServiceName;
    @ApiModelProperty(value = "系统服务类型")
    private String sysServiceType;
    @ApiModelProperty(value = "操作类型")
    private String type;
    @Lob
    @Column(columnDefinition = "TEXT")
    @ApiModelProperty(value = "操作结果")
    private String result;
    @ApiModelProperty(value = "启动时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @ApiModelProperty(value = "结束时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSysServiceName() {
        return sysServiceName;
    }

    public void setSysServiceName(String sysServiceName) {
        this.sysServiceName = sysServiceName;
    }

    public String getSysServiceType() {
        return sysServiceType;
    }

    public void setSysServiceType(String sysServiceType) {
        this.sysServiceType = sysServiceType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
