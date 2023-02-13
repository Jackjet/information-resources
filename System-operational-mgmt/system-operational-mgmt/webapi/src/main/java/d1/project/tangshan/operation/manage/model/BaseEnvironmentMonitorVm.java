package d1.project.tangshan.operation.manage.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;

@ApiModel(value = "BaseEnvironmentMonitorVm", description = "基础环境监控")
public class BaseEnvironmentMonitorVm {
    @ApiModelProperty("节点名称")
    private String nodeName;

    @ApiModelProperty("节点ID")
    private String nodeId;

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

    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "日志创建时间", example = "2018-01-23 09:12:32")
    private Calendar createTime;

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

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }
}