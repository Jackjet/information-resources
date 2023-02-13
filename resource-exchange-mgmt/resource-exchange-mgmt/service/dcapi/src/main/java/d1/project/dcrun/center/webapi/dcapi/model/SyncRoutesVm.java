package d1.project.dcrun.center.webapi.dcapi.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author lin
 */
@ApiModel(value = "SyncRoutesVm", description = "api同步")
public class SyncRoutesVm {
    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("api名称")
    private String name;
    @ApiModelProperty(value = "源api属性(路由属性)", notes = "GET 、PUT、 DELETE、 POST")
    private String sourceMethod;
    @ApiModelProperty(value = "源api地址(路由地址)", notes = "源路径")
    private String sourcePath;
    @ApiModelProperty(value = "目标api属性(service属性)", notes = "GET 、PUT、 DELETE、 POST")
    private String targetMethod;
    @ApiModelProperty(value = "目标api地址(service地址)", notes = "目标路径")
    private String targetPath;
    @ApiModelProperty("访问控制策略id")
    private String accessStrategyId;
    @ApiModelProperty("访问控制策略类型")
    private String accessType;
    @ApiModelProperty("访问控制策略动作")
    private String operation;
    @ApiModelProperty("访问控制策略ips")
    private String ips;
    @ApiModelProperty("访问控制策略appIds")
    private String appIds;
    @ApiModelProperty("流量控制策略id")
    private String flowStrategyId;
    @ApiModelProperty("流量控制策略类型")
    private String flowType;
    @ApiModelProperty(value = "时长")
    private Integer time;
    @ApiModelProperty("API流量限制")
    private Integer apiFlowLimit;
    @ApiModelProperty(value = "用户流量限制")
    private Integer appIdFlowLimit;
    @ApiModelProperty(value = "源IP流量限制")
    private Integer ipFlowLimit;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceMethod() {
        return sourceMethod;
    }

    public void setSourceMethod(String sourceMethod) {
        this.sourceMethod = sourceMethod;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getTargetMethod() {
        return targetMethod;
    }

    public void setTargetMethod(String targetMethod) {
        this.targetMethod = targetMethod;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }

    public String getAccessStrategyId() {
        return accessStrategyId;
    }

    public void setAccessStrategyId(String accessStrategyId) {
        this.accessStrategyId = accessStrategyId;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getIps() {
        return ips;
    }

    public void setIps(String ips) {
        this.ips = ips;
    }

    public String getAppIds() {
        return appIds;
    }

    public void setAppIds(String appIds) {
        this.appIds = appIds;
    }

    public String getFlowStrategyId() {
        return flowStrategyId;
    }

    public void setFlowStrategyId(String flowStrategyId) {
        this.flowStrategyId = flowStrategyId;
    }

    public String getFlowType() {
        return flowType;
    }

    public void setFlowType(String flowType) {
        this.flowType = flowType;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getApiFlowLimit() {
        return apiFlowLimit;
    }

    public void setApiFlowLimit(Integer apiFlowLimit) {
        this.apiFlowLimit = apiFlowLimit;
    }

    public Integer getAppIdFlowLimit() {
        return appIdFlowLimit;
    }

    public void setAppIdFlowLimit(Integer appIdFlowLimit) {
        this.appIdFlowLimit = appIdFlowLimit;
    }

    public Integer getIpFlowLimit() {
        return ipFlowLimit;
    }

    public void setIpFlowLimit(Integer ipFlowLimit) {
        this.ipFlowLimit = ipFlowLimit;
    }
}
