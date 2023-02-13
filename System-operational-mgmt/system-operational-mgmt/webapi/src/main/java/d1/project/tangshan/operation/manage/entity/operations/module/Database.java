package d1.project.tangshan.operation.manage.entity.operations.module;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author lin
 */
@Entity
@Table(name = "d1_database")
@ApiModel(value = "Database", description = "数据库")
public class Database extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("节点Id")
    private String nodeId;
    @ApiModelProperty("节点名称")
    private String nodeName;
    @ApiModelProperty("进程名")
    private String processName;
    @ApiModelProperty("占用端口")
    private String port;
    @ApiModelProperty("所属系统和平台")
    private String systemAndPlatform;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("数据库类型（Mysql,Postgresql）")
    private String type;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSystemAndPlatform() {
        return systemAndPlatform;
    }

    public void setSystemAndPlatform(String systemAndPlatform) {
        this.systemAndPlatform = systemAndPlatform;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
