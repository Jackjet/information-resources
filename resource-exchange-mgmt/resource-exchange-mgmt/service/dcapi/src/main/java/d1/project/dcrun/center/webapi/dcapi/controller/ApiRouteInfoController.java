package d1.project.dcrun.center.webapi.dcapi.controller;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.common.PageableVm;
import d1.project.dcrun.center.webapi.dcapi.entity.ApiRouteInfo;
import d1.project.dcrun.center.webapi.dcapi.entity.view.ApiView;
import d1.project.dcrun.center.webapi.dcapi.service.ApiRouteInfoService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author lin
 */
@Auth("integration")
@RestController
@RequestMapping("/integration/apiRoute")
@Api(value = "/integration/apiRoute", tags = "路由管理")
public class ApiRouteInfoController {
    @Autowired
    private ApiRouteInfoService apiRouteInfoService;

    @ApiOperation(value = "查询API信息", notes = "查询API信息")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<ApiView>> findAll(@Valid ApiRouteInfoFindAllVm data) {
        try {
            JSONObject jo = (JSONObject) JSONObject.toJSON(data);
            Page<ApiView> result = apiRouteInfoService.findAll(jo);
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "获取数据详细信息", notes = "根据id来获取详细信息")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/find", method = {RequestMethod.GET})
    public Result<ApiView> apiFindById(String id) {
        try {
            ApiView result = apiRouteInfoService.find(id);
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "新增API信息")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(@RequestBody @Valid ApiRouteInfoInsertVm apiRouteInfoInsertVm, HttpServletRequest request) {
        try {
            JSONObject jo = (JSONObject) JSONObject.toJSON(apiRouteInfoInsertVm);
            apiRouteInfoService.insert(jo, request);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "编辑API信息")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(@RequestBody @Valid ApiRouteInfoUpdateVm data, HttpServletRequest request) {
        try {
            JSONObject jo = (JSONObject) JSONObject.toJSON(data);
            apiRouteInfoService.update(jo, request);
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "同步路由数据，生成路由数据json文件，发送MQTT至微应用，同步路由数据")
    @RequestMapping(value = "/syncRoutes", method = RequestMethod.PUT)
    public Result<String> syncRoutes(@RequestBody @Valid SyncRoutes model) {
        try {
            JSONObject jo = (JSONObject) JSONObject.toJSON(model);
            apiRouteInfoService.syncRoutes(jo);
            return ResultUtil.success("同步成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "获取数据详细信息", notes = "根据id来获取详细信息")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result<ApiRouteInfo> findById(String id) {
        ApiRouteInfo mUser = apiRouteInfoService.findById(id);
        return mUser != null ? ResultUtil.success("获取详细信息", mUser) : ResultUtil.fail("数据为空或获取失败!");
    }


    @ApiOperation(value = "删除数据", notes = "根据id来指定删除数据")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    public Result delete(String id) {
        try {
            apiRouteInfoService.delete(id);
        } catch (Exception e) {
            return ResultUtil.fail("删除失败:" + e.getMessage());
        }
        return ResultUtil.success("删除成功!");
    }
}

@ApiModel(value = "ApiRouteInfoFindAllVm", description = "api查询")
class ApiRouteInfoFindAllVm extends PageableVm {
    @ApiModelProperty(value = "集成项目id")
    @NotBlank(message = "集成项目id不能为空")
    private String integrationId;
    @ApiModelProperty(value = "系统服务id")
    @NotBlank(message = "系统服务id不能为空")
    private String sysServiceId;
    @ApiModelProperty(value = "api名称")
    private String name;
    @ApiModelProperty(value = "请求路径")
    private String sourcePath;
    @ApiModelProperty(value = "ip地址")
    private String targetPath;

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public String getSysServiceId() {
        return sysServiceId;
    }

    public void setSysServiceId(String sysServiceId) {
        this.sysServiceId = sysServiceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public void setTargetPath(String targetPath) {
        this.targetPath = targetPath;
    }
}

@ApiModel(value = "ApiRouteInfoInsertVm", description = "api添加")
class ApiRouteInfoInsertVm {
    @ApiModelProperty(value = "集成项目id")
    @NotBlank(message = "集成项目id不能为空")
    private String integrationId;
    @ApiModelProperty(value = "系统服务id")
    @NotBlank(message = "系统服务id不能为空")
    private String sysServiceId;
    @ApiModelProperty("api名称")
    @NotBlank(message = "路由名称不可为空")
    private String name;
    @ApiModelProperty(value = "源api属性(路由属性)", notes = "GET 、PUT、 DELETE、 POST")
    @NotBlank(message = "路由属性不可为空")
    private String sourceMethod;
    @ApiModelProperty(value = "源api地址(路由地址)", notes = "源路径")
    @NotBlank(message = "路由地址不可为空")
    private String sourcePath;
    @ApiModelProperty(value = "目标api属性(service属性)", notes = "GET 、PUT、 DELETE、 POST")
    @NotBlank(message = "service属性不可为空")
    private String targetMethod;
    @ApiModelProperty(value = "目标api地址(service地址)", notes = "源路径")
    @NotBlank(message = "service地址不可为空")
    private String targetPath;
    @ApiModelProperty("访问控制策略id")
    @NotBlank(message = "访问控制策略id不可为空")
    private String accessStrategyId;
    @ApiModelProperty(value = "流量控制策略id")
    @NotBlank(message = "流量控制策略id不可为空")
    private String flowStrategyId;
    @ApiModelProperty("备注")
    private String remark;

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public String getSysServiceId() {
        return sysServiceId;
    }

    public void setSysServiceId(String sysServiceId) {
        this.sysServiceId = sysServiceId;
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

    public String getFlowStrategyId() {
        return flowStrategyId;
    }

    public void setFlowStrategyId(String flowStrategyId) {
        this.flowStrategyId = flowStrategyId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "ApiRouteInfoUpdateVm", description = "api编辑")
class ApiRouteInfoUpdateVm {
    @ApiModelProperty("主键id")
    @NotBlank(message = "id不能为空")
    private String id;
    @ApiModelProperty(value = "集成项目id")
    @NotBlank(message = "集成项目id不能为空")
    private String integrationId;
    @ApiModelProperty(value = "系统服务id")
    @NotBlank(message = "系统服务id不能为空")
    private String sysServiceId;
    @ApiModelProperty("api名称")
    @NotBlank(message = "路由名称不可为空")
    private String name;
    @ApiModelProperty(value = "源api属性(路由属性)", notes = "GET 、PUT、 DELETE、 POST")
    @NotBlank(message = "路由属性不可为空")
    private String sourceMethod;
    @ApiModelProperty(value = "源api地址(路由地址)", notes = "源路径")
    @NotBlank(message = "路由地址不可为空")
    private String sourcePath;
    @ApiModelProperty(value = "目标api属性(service属性)", notes = "GET 、PUT、 DELETE、 POST")
    @NotBlank(message = "service属性不可为空")
    private String targetMethod;
    @ApiModelProperty(value = "目标api地址(service地址)", notes = "源路径")
    @NotBlank(message = "service地址不可为空")
    private String targetPath;
    @ApiModelProperty("访问控制策略id")
    @NotBlank(message = "访问控制策略id不可为空")
    private String accessStrategyId;
    @ApiModelProperty(value = "流量控制策略id")
    @NotBlank(message = "流量控制策略id不可为空")
    private String flowStrategyId;
    @ApiModelProperty("备注")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public String getSysServiceId() {
        return sysServiceId;
    }

    public void setSysServiceId(String sysServiceId) {
        this.sysServiceId = sysServiceId;
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

    public String getFlowStrategyId() {
        return flowStrategyId;
    }

    public void setFlowStrategyId(String flowStrategyId) {
        this.flowStrategyId = flowStrategyId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "SyncRoutes", description = "api同步")
class SyncRoutes {
    @ApiModelProperty(value = "集成项目id")
    @NotBlank(message = "集成项目id不能为空")
    private String integrationId;
    @ApiModelProperty(value = "系统服务id")
    @NotBlank(message = "系统服务id不能为空")
    private String sysServiceId;

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public String getSysServiceId() {
        return sysServiceId;
    }

    public void setSysServiceId(String sysServiceId) {
        this.sysServiceId = sysServiceId;
    }
}

@ApiModel(value = "SyncRoutesVm", description = "api同步")
class SyncRoutesVm {
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


