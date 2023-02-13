package d1.project.tangshan.operation.manage.entity.database;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author lin
 */
@Entity
@Table(name = "d1_database_monitoring")
@ApiModel(value = "DatabaseMonitoring", description = "数据库监控")
public class DatabaseMonitoring extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("节点id")
    private String nodeId;
    @ApiModelProperty("节点名称")
    private String nodeName;
    @ApiModelProperty("数据库id")
    private String databaseId;
    @ApiModelProperty("数据库名称")
    private String databaseName;
    @ApiModelProperty("内存阈值")
    private Long ram;
    @ApiModelProperty("存储阈值")
    private Long rom;
    @ApiModelProperty("告警方式0 短信；1 邮件")
    private String alarmWay;
    @ApiModelProperty("通知人(以英文 , 隔开)")
    private String peopleNotified;

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

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
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
