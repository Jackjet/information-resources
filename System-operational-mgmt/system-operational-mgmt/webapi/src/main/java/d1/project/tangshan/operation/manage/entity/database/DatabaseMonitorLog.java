package d1.project.tangshan.operation.manage.entity.database;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import d1.framework.webapi.entity.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;

/**
 * @author libin
 */
@Entity
@Table(name = "d1_database_monitor_log")
@ApiModel(value = "DatabaseMonitorLog", description = "数据库监控日志")
public class DatabaseMonitorLog extends BaseCreateEntity {
    @ApiModelProperty("节点")
    private String nodeName;

    @ApiModelProperty("节点id")
    private String nodeId;

    @ApiModelProperty("数据库")
    private String database;

    @ApiModelProperty("数据库id")
    private String databaseId;

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

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
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
}
