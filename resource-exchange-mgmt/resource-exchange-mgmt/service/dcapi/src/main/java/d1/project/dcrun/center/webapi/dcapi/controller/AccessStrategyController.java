package d1.project.dcrun.center.webapi.dcapi.controller;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.common.PageableVm;
import d1.project.dcrun.center.webapi.dcapi.entity.AccessControlPlan;
import d1.project.dcrun.center.webapi.dcapi.service.AccessControlPlanService;
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
@RequestMapping("/integration/accessStrategy")
@Api(value = "/integration/accessStrategy", tags = "访问控制策略信息管理")
public class AccessStrategyController {
    @Autowired
    private AccessControlPlanService accessControlPlanService;

    @ApiOperation(value = "查询访问控制策略信息", notes = "")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<AccessControlPlan>> findAll(@Valid AccessControlPlanFindAllVm data) {
        try {
            Page<AccessControlPlan> result = accessControlPlanService.findAll((JSONObject) JSONObject.toJSON(data));
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "新增访问控制策略信息")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(@RequestBody @Valid AccessControlPlanInsertVm data, HttpServletRequest request) {
        try {
            accessControlPlanService.insert((JSONObject) JSONObject.toJSON(data), request);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "编辑访问控制策略信息")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(@RequestBody @Valid AccessControlPlanUpdateVm data, HttpServletRequest request) {
        try {
            accessControlPlanService.update((JSONObject) JSONObject.toJSON(data), request);
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "获取数据详细信息", notes = "根据id来获取详细信息")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result<AccessControlPlan> findById(String id) {
        AccessControlPlan mUser = accessControlPlanService.findById(id);
        return mUser != null ? ResultUtil.success("获取详细信息", mUser) : ResultUtil.fail("数据为空或获取失败!");
    }


    @ApiOperation(value = "删除数据", notes = "根据id来指定删除数据")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    public Result delete(String id) {
        try {
            accessControlPlanService.delete(id);
        } catch (Exception e) {
            return ResultUtil.fail("删除失败:" + e.getMessage());
        }
        return ResultUtil.success("删除成功!");
    }
}

@ApiModel(value = "AccessControlPlanFindAllVm", description = "访问控制策略信息查询")
class AccessControlPlanFindAllVm extends PageableVm {
    @ApiModelProperty(value = "集成项目id")
    @NotBlank(message = "集成项目id不能为空")
    private String integrationId;
    @ApiModelProperty(value = "系统服务id")
    @NotBlank(message = "系统服务id不能为空")
    private String sysServiceId;
    @ApiModelProperty(value = "访问控制策略名称")
    private String name;
    @ApiModelProperty(value = "策略类型(0：IP地址、1：账户名)")
    private String type;
    @ApiModelProperty(value = "动作(0：允许、1：禁止)")
    private String operation;

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

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }
}

@ApiModel(value = "AccessControlPlanInsertVm", description = "访问控制策略信息添加")
class AccessControlPlanInsertVm {
    @ApiModelProperty(value = "集成项目id")
    @NotBlank(message = "集成项目id不能为空")
    private String integrationId;
    @ApiModelProperty(value = "系统服务id")
    @NotBlank(message = "系统服务id不能为空")
    private String sysServiceId;
    @ApiModelProperty("访问控制策略名称")
    @NotBlank(message = "访问控制策略名称不可为空")
    private String name;
    @ApiModelProperty("限制类型(0：IP地址、1：账户名)")
    @NotBlank(message = "策略类型不可为空")
    private String type;
    @ApiModelProperty("动作(0：允许、1：禁止)")
    @NotBlank(message = "动作不可为空")
    private String operation;
    @ApiModelProperty("IP地址(为空时，不限制,以逗号分隔)")
    private String ips;
    @ApiModelProperty("账户名(为空时，不限制,以逗号分隔)")
    private String appIds;

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
}

@ApiModel(value = "AccessControlPlanUpdateVm", description = "访问控制策略信息编辑")
class AccessControlPlanUpdateVm {
    @ApiModelProperty("主键id")
    private String id;
    @ApiModelProperty("访问控制策略名称")
    @NotBlank(message = "访问控制策略名称不可为空")
    private String name;
    @ApiModelProperty("限制类型(0：IP地址、1：账户名)")
    @NotBlank(message = "策略类型不可为空")
    private String type;
    @ApiModelProperty("动作(0：允许、1：禁止)")
    @NotBlank(message = "动作不可为空")
    private String operation;
    @ApiModelProperty("IP地址(为空时，不限制,以逗号分隔)")
    private String ips;
    @ApiModelProperty("账户名(为空时，不限制,以逗号分隔)")
    private String appIds;

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
}
