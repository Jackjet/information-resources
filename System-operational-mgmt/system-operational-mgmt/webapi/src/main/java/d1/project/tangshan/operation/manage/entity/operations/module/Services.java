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
@Table(name = "d1_services")
@ApiModel(value = "Services", description = "服务")
public class Services extends BaseCreateAndUpdateEntity {
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
    @ApiModelProperty("备注")
    private String remark;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
