package d1.project.dcrun.center.webapi.system.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "CoreSysServiceServiceInfoVm", description = "查询系统服务状态及版本号")
public class CoreSysServiceServiceInfoVm {
    @ApiModelProperty(value = "当前版本号")
    private String currentVersion;
    @ApiModelProperty(value = "远程版本号")
    private String remoteVersion;
    @ApiModelProperty(value = "状态")
    private String status;

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public String getRemoteVersion() {
        return remoteVersion;
    }

    public void setRemoteVersion(String remoteVersion) {
        this.remoteVersion = remoteVersion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
