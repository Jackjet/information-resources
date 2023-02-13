package d1.project.dcrun.center.webapi.dcapi.controller;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.common.PageableVm;
import d1.project.dcrun.center.webapi.dcapi.entity.FlowControlPlan;
import d1.project.dcrun.center.webapi.dcapi.service.FlowControlPlanService;
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
@RequestMapping("/integration/flowStrategy")
@Api(value = "/integration/flowStrategy", tags = "流量控制策略信息管理")
public class FlowStrategyController {
    @Autowired
    private FlowControlPlanService flowControlPlanService;

    @ApiOperation(value = "查询流量控制策略信息", notes = "")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<FlowControlPlan>> findAll(@Valid FlowControlPlanFindAllVm data) {
        try {
            Page<FlowControlPlan> result = flowControlPlanService.findAll((JSONObject) JSONObject.toJSON(data));
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "新增流量控制策略信息")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(@RequestBody @Valid FlowControlPlanInsertVm data, HttpServletRequest request) {
        try {
            flowControlPlanService.insert((JSONObject) JSONObject.toJSON(data), request);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "编辑流量控制策略信息")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(@RequestBody @Valid FlowControlPlanUpdateVm data, HttpServletRequest request) {
        try {
            flowControlPlanService.update((JSONObject) JSONObject.toJSON(data), request);
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "获取数据详细信息", notes = "根据id来获取详细信息")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result<FlowControlPlan> findById(String id) {
        FlowControlPlan mUser = flowControlPlanService.findById(id);
        return mUser != null ? ResultUtil.success("获取详细信息", mUser) : ResultUtil.fail("数据为空或获取失败!");
    }


    @ApiOperation(value = "删除数据", notes = "根据id来指定删除数据")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    public Result delete(String id) {
        try {
            flowControlPlanService.delete(id);
        } catch (Exception e) {
            return ResultUtil.fail("删除失败:" + e.getMessage());
        }
        return ResultUtil.success("删除成功!");
    }
}

@ApiModel(value = "FlowControlPlanFindAllVm", description = "流量控制策略信息查询")
class FlowControlPlanFindAllVm extends PageableVm {
    @ApiModelProperty(value = "集成项目id")
    @NotBlank(message = "集成项目id不能为空")
    private String integrationId;
    @ApiModelProperty(value = "系统服务id")
    @NotBlank(message = "系统服务id不能为空")
    private String sysServiceId;
    @ApiModelProperty(value = "流量控制策略名称")
    private String name;
    @ApiModelProperty(value = "流量策略类型(0：无限制、1：账户名、2：IP地址)")
    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

@ApiModel(value = "FlowControlPlanInsertVm", description = "流量控制策略信息添加")
class FlowControlPlanInsertVm {
    @ApiModelProperty(value = "集成项目id")
    @NotBlank(message = "集成项目id不能为空")
    private String integrationId;
    @ApiModelProperty(value = "系统服务id")
    @NotBlank(message = "系统服务id不能为空")
    private String sysServiceId;
    @ApiModelProperty("流量控制策略名称")
    @NotBlank(message = "访问流量策略名称不可为空")
    private String name;
    @ApiModelProperty("流量策略类型(0：无限制、1：账户名、2：IP地址)")
    private String type;
    @ApiModelProperty("时长(以天为单位)")
    private Integer time;
    @ApiModelProperty("API流量限制(-1表示无限制)")
    private Integer apiFlowLimit;
    @ApiModelProperty("用户流量限制")
    private Integer appIdFlowLimit;
    @ApiModelProperty("源IP流量限制")
    private Integer ipFlowLimit;
    @ApiModelProperty("描述")
    private String description;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

@ApiModel(value = "FlowControlPlanUpdateVm", description = "流量控制策略信息编辑")
class FlowControlPlanUpdateVm {
    @ApiModelProperty("主键id")
    @NotBlank(message = "id不能为空")
    private String id;
    @ApiModelProperty("流量控制策略名称")
    @NotBlank(message = "流量控制策略名称不可为空")
    private String name;
    @ApiModelProperty("流量策略类型(0：无限制、1：账户名、2：IP地址)")
    private String type;
    @ApiModelProperty("时长(以天为单位)")
    private Integer time;
    @ApiModelProperty("API流量限制(-1表示无限制)")
    private Integer apiFlowLimit;
    @ApiModelProperty("用户流量限制")
    private Integer appIdFlowLimit;
    @ApiModelProperty("源IP流量限制")
    private Integer ipFlowLimit;
    @ApiModelProperty("描述")
    private String description;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

