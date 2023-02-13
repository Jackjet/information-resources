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
@Table(name = "d1_database_backup")
@ApiModel(value = "DatabaseBackup", description = "备份和恢复")
public class DatabaseBackup extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("节点id")
    private String nodeId;
    @ApiModelProperty("节点名称")
    private String nodeName;
    @ApiModelProperty("数据库id")
    private String databaseId;
    @ApiModelProperty("数据库名称")
    private String databaseName;
    @ApiModelProperty("实例Id")
    private String instanceId;
    @ApiModelProperty("实例名称")
    private String instanceName;
    @ApiModelProperty("备份位置")
    private String location;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("备份结果 0 1 2")
    private String result;

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

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
