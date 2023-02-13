package d1.project.tangshan.operation.manage.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "NodeStatusAnalysisVm", description = "节点状态分析")
public class NodeStatusAnalysisVm {

    @ApiModelProperty("月份")
    private String month;

    @ApiModelProperty("计数")
    private Long count;

    @ApiModelProperty("节点id")
    private String nodeId;

    @ApiModelProperty("状态")
    private String status;

    public NodeStatusAnalysisVm() {
    }

    public NodeStatusAnalysisVm(String month, Long count, String nodeId, String status) {
        this.month = month;
        this.count = count;
        this.nodeId = nodeId;
        this.status = status;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
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
}
