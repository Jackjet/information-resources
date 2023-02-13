package d1.project.tangshan.operation.manage.entity;

import d1.framework.webapi.entity.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;

/**
 * @author Buter
 * @date 2020/3/20 14:40
 */
@Entity
@Table(name = "d1_base_environment_monitor")
@ApiModel(value = "BaseEnvironmentMonitorEntity", description = "基础环境监控")
public class BaseEnvironmentMonitorEntity extends BaseCreateEntity {
    @ApiModelProperty("内网IP地址")
    private String intranetIp;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("网络")
    private String network;

    @ApiModelProperty("数据库")
    private String db;

    @ApiModelProperty("系统软件")
    private String systemSoftware;

    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "日志记录时间", example = "2018-01-23 09:12:32")
    private Calendar logTime;

    public String getIntranetIp() {
        return intranetIp;
    }

    public void setIntranetIp(String intranetIp) {
        this.intranetIp = intranetIp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getSystemSoftware() {
        return systemSoftware;
    }

    public void setSystemSoftware(String systemSoftware) {
        this.systemSoftware = systemSoftware;
    }

    public Calendar getLogTime() {
        return logTime;
    }

    public void setLogTime(Calendar logTime) {
        this.logTime = logTime;
    }
}
