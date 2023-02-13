package d1.project.tangshan.operation.manage.entity;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Calendar;

/**
 * @author Buter
 * @date 2020/3/20 14:40
 */
@Entity
@Table(name = "d1_automated_deployment_log")
@ApiModel(value = "AutomatedDeploymentLogEntity", description = "自动化部署日志")
public class AutomatedDeploymentLogEntity extends BaseCreateAndUpdateEntity {

    @ApiModelProperty("安装路径id")
    private String installPathId;

    @ApiModelProperty("安装路径")
    private String installPath;

    @ApiModelProperty("操作系统")
    private String installPathOs;

    @ApiModelProperty("节点id")
    private String nodeId;

    @ApiModelProperty("节点名称")
    private String nodeName;

    @ApiModelProperty("系统id")
    private String sysId;

    @ApiModelProperty("系统名称")
    private String sysName;

    @ApiModelProperty("部署类型:")
    private String type;

    @ApiModelProperty("接口参数")
    private String apiParams;

    @ApiModelProperty("版本号")
    private String versionNumber;

    @ApiModelProperty("部署包路径")
    private String automatedPackPath;

    @ApiModelProperty("请求body")
    @Column(columnDefinition = "TEXT")
    private String apiBody;

    @ApiModelProperty("执行结果")
    private String result;

    @ApiModelProperty("执行结果")
    @Column(columnDefinition = "TEXT")
    private String resultLog;

    @ApiModelProperty("自动化部署id")
    private String automatedDeploymentEntityId;

    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "开始时间", example = "2018-01-23 09:12:32")
    private Calendar beginTime;

    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "结束时间", example = "2018-01-23 09:12:32")
    private Calendar endTime;

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

    public String getResultLog() {
        return resultLog;
    }

    public void setResultLog(String resultLog) {
        this.resultLog = resultLog;
    }

    public String getAutomatedPackPath() {
        return automatedPackPath;
    }

    public void setAutomatedPackPath(String automatedPackPath) {
        this.automatedPackPath = automatedPackPath;
    }

    public String getAutomatedDeploymentEntityId() {
        return automatedDeploymentEntityId;
    }

    public void setAutomatedDeploymentEntityId(String automatedDeploymentEntityId) {
        this.automatedDeploymentEntityId = automatedDeploymentEntityId;
    }

    public String getInstallPathId() {
        return installPathId;
    }

    public void setInstallPathId(String installPathId) {
        this.installPathId = installPathId;
    }

    public String getInstallPath() {
        return installPath;
    }

    public void setInstallPath(String installPath) {
        this.installPath = installPath;
    }

    public String getInstallPathOs() {
        return installPathOs;
    }

    public void setInstallPathOs(String installPathOs) {
        this.installPathOs = installPathOs;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
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

    public String getApiParams() {
        return apiParams;
    }

    public void setApiParams(String apiParams) {
        this.apiParams = apiParams;
    }

    public String getApiBody() {
        return apiBody;
    }

    public void setApiBody(String apiBody) {
        this.apiBody = apiBody;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Calendar getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Calendar beginTime) {
        this.beginTime = beginTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }
}
