package d1.project.dcrun.center.webapi.common.service.operation.record.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "OperationRecordInsertVm", description = "OperationRecord 新增服务操作记录")
public class OperationRecordInsertVm {
    @ApiModelProperty(value = "系统服务appid")
    private String appid;
    @ApiModelProperty(value = "系统服务名称")
    private String sysServiceName;
    @ApiModelProperty(value = "系统服务类型")
    private String sysServiceType;
    @ApiModelProperty(value = "操作类型")
    private String type;

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
}
