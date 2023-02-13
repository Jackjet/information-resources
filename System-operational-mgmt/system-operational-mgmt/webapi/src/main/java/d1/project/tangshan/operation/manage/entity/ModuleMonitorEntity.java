package d1.project.tangshan.operation.manage.entity;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Buter
 * @date 2020/3/20 14:40
 */
@Entity
@Table(name = "d1_module_monitor")
@ApiModel(value = "ModuleMonitorEntity", description = "模块监控")
public class ModuleMonitorEntity extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("监控类型，性能监控(performance)、数据监控(data)、资源监控(resources)")
    private String type;

    @ApiModelProperty("节点id")
    private String nodeId;

    @ApiModelProperty("节点名称")
    private String nodeName;

    @ApiModelProperty("系统id")
    private String sysId;

    @ApiModelProperty("系统名称")
    private String sysName;

    @ApiModelProperty("用户访问次数阈值/日")
    private Integer access;

    @ApiModelProperty("内存阈值(M)")
    private Long ram;

    @ApiModelProperty("存储阈值(M)")
    private Long rom;

    @ApiModelProperty("cpu使用阈值(%)")
    private Integer cpu;

    @ApiModelProperty("告警方式，短信(sms)、邮件(email)、锁定账号(account)")
    private String alarmWay;

    @ApiModelProperty("通知人")
    private String peopleNotified;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public Integer getAccess() {
        return access;
    }

    public void setAccess(Integer access) {
        this.access = access;
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

    public String getAlarmWay() {
        return alarmWay;
    }

    public void setAlarmWay(String alarmWay) {
        this.alarmWay = alarmWay;
    }

    public String getPeopleNotified() {
        return peopleNotified;
    }

    public void setPeopleNotified(String peopleNotified) {
        this.peopleNotified = peopleNotified;
    }
}
