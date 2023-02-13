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
@Table(name = "d1_resource_monitor")
@ApiModel(value = "ResourceMonitorEntity", description = "资源监控")
public class ResourceMonitorEntity extends BaseCreateEntity {
    @ApiModelProperty("内网IP地址")
    private String intranetIp;

    @ApiModelProperty("ram")
    private Long ram;

    @ApiModelProperty("rom")
    private Long rom;

    @ApiModelProperty("cpu")
    private Integer cpu;

    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "日志记录时间", example = "2018-01-23 09:12:32")
    private Calendar logTime;

    public String getIntranetIp() {
        return intranetIp;
    }

    public void setIntranetIp(String intranetIp) {
        this.intranetIp = intranetIp;
    }

    public Long getRam() {
        return ram;
    }

    public void setRam(Long ram) {
        this.ram = ram;
    }

    public Long getRom() {
        return rom;
    }

    public void setRom(Long rom) {
        this.rom = rom;
    }

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Calendar getLogTime() {
        return logTime;
    }

    public void setLogTime(Calendar logTime) {
        this.logTime = logTime;
    }
}
