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
@Table(name = "d1_system_monitor")
@ApiModel(value = "SystemMonitorEntity", description = "系统监控")
public class SystemMonitorEntity extends BaseCreateEntity {
    @ApiModelProperty("节点")
    private String nodeName;

    @ApiModelProperty("节点")
    private String nodeId;

    @ApiModelProperty("系统")
    private String system;

    @ApiModelProperty("系统id")
    private String systemId;

    @ApiModelProperty("数据资源")
    private String dataSource;

    @ApiModelProperty("内存资源")
    private String memorySource;

    @ApiModelProperty("运行状态")
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "日志记录时间", example = "2018-01-23 09:12:32")
    private Calendar logTime;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getMemorySource() {
        return memorySource;
    }

    public void setMemorySource(String memorySource) {
        this.memorySource = memorySource;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Calendar getLogTime() {
        return logTime;
    }

    public void setLogTime(Calendar logTime) {
        this.logTime = logTime;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }
}
