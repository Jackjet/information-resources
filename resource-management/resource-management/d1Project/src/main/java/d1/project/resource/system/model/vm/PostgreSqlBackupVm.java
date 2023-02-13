package d1.project.resource.system.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-14 20:04
 */
@ApiModel("postgresql备份配置")
public class PostgreSqlBackupVm {

    @ApiModelProperty("cron标准写法")
    private String cron;

    @ApiModelProperty("启用")
    private boolean enable;

    @ApiModelProperty("路径")
    private String path;

    public PostgreSqlBackupVm() {
        this.setEnable(false);
        this.setCron("");
        this.setPath("");
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
