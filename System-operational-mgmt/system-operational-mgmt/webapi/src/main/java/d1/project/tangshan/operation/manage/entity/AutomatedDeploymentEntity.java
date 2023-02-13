package d1.project.tangshan.operation.manage.entity;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * @author Buter
 * @date 2020/3/20 14:40
 */
@Entity
@Table(name = "d1_automated_deployment")
@ApiModel(value = "AutomatedDeploymentEntity", description = "自动化部署管理")
public class AutomatedDeploymentEntity extends BaseCreateAndUpdateEntity {

    @ApiModelProperty("安装路径id")
    private String installPathId;

    @ApiModelProperty("节点id")
    private String nodeId;

    @ApiModelProperty("节点名称")
    private String nodeName;

    @ApiModelProperty("系统id")
    private String sysId;

    @ApiModelProperty("系统名称")
    private String sysName;

    @ApiModelProperty("版本号")
    private String versionNumber;

    @ApiModelProperty("部署包路径")
    private String automatedPackPath;

    @ApiModelProperty("部署类型:deployment,upgrade,configUpdate,")
    private String type;

    @ApiModelProperty("部署阶段:未部署:NotDeployed,部署中:Deploying,已部署:Deployed,不可部署:NotDeployAble,,初次部署InitDeployAble")
    private String typeStage;

    @ApiModelProperty("部署接口")
    private String apiId;

    @ApiModelProperty("接口名称")
    private String apiName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("接口")
    private String apiUrl;
    @ApiModelProperty("接口请求方式：get，post，put，delete")
    private String apiMethod;

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiMethod() {
        return apiMethod;
    }

    public void setApiMethod(String apiMethod) {
        this.apiMethod = apiMethod;
    }

    public String getTypeStage() {
        return typeStage;
    }

    public void setTypeStage(String typeStage) {
        this.typeStage = typeStage;
    }

    public String getInstallPathId() {
        return installPathId;
    }

    public void setInstallPathId(String installPathId) {
        this.installPathId = installPathId;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getAutomatedPackPath() {
        return automatedPackPath;
    }

    public void setAutomatedPackPath(String automatedPackPath) {
        this.automatedPackPath = automatedPackPath;
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

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
