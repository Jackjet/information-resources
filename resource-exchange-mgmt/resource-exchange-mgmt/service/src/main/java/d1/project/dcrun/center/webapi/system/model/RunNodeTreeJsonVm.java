package d1.project.dcrun.center.webapi.system.model;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * @author libin
 */
public class RunNodeTreeJsonVm {
    private Integer id;
    private String nodeId;
    @ApiModelProperty(value = "密钥")
    private String appkey;
    @ApiModelProperty(value = "集成项目id")
    private String integrationId;
    @ApiModelProperty(value = "运行节点名称")
    private String name;
    @ApiModelProperty(value = "父节点id")
    private String parentNodeId;
    @ApiModelProperty(value = "路径")
    private String path;
    @ApiModelProperty(value = "安装文档")
    private String installUrl;
    @ApiModelProperty(value = "最后上报时间")
    private Date reportTime;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "更新人id")
    private String updateById;
    @ApiModelProperty(value = "更新人名称")
    private String updateByName;
    @ApiModelProperty(value = "更新时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    @ApiModelProperty(value = "创建人id")
    private String createById;
    @ApiModelProperty(value = "创建人名称")
    private String createByName;
    @ApiModelProperty(value = "创建时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @ApiModelProperty(value = "状态")
    private String status;
    private List<RunNodeTreeJsonVm> children;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentNodeId() {
        return parentNodeId;
    }

    public void setParentNodeId(String parentNodeId) {
        this.parentNodeId = parentNodeId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getInstallUrl() {
        return installUrl;
    }

    public void setInstallUrl(String installUrl) {
        this.installUrl = installUrl;
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUpdateById() {
        return updateById;
    }

    public void setUpdateById(String updateById) {
        this.updateById = updateById;
    }

    public String getUpdateByName() {
        return updateByName;
    }

    public void setUpdateByName(String updateByName) {
        this.updateByName = updateByName;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RunNodeTreeJsonVm> getChildren() {
        return children;
    }

    public void setChildren(List<RunNodeTreeJsonVm> children) {
        this.children = children;
    }
}

