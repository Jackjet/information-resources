package d1.project.dcrun.center.webapi.system.entity;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author libin
 */
@Entity
@Table(name = "d1_run_node")
@ApiModel(value = "RunNode", description = "运行节点表")
public class RunNode extends BaseCreateAndUpdateEntity {
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
    @ApiModelProperty(value = "windows安装文档")
    private String installBatUrl;
    @ApiModelProperty(value = "最后上报时间")
    private Date reportTime;
    @ApiModelProperty(value = "备注")
    private String remark;

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

    public String getInstallBatUrl() {
        return installBatUrl;
    }

    public void setInstallBatUrl(String installBatUrl) {
        this.installBatUrl = installBatUrl;
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
}
