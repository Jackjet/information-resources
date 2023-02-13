package d1.project.dcrun.center.webapi.dcapi.controller;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.common.PageableVm;
import d1.project.dcrun.center.webapi.dcapi.entity.ApiRouteUser;
import d1.project.dcrun.center.webapi.dcapi.service.ApiRouteUserService;
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
 * @author xuaa
 */
@Auth("integration")
@RestController
@RequestMapping("/integration/apiRouteUser")
@Api(value = "/integration/apiRouteUser", tags = "Api开发者信息")
public class ApiRouteUserController {
    @Autowired
    private ApiRouteUserService apiRouteUserService;

    @ApiOperation(value = "查询Api开发者信息", notes = "查询Api开发者信息")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<ApiRouteUser>> findAll(@Valid ApiRouteUserFindAllVm data) {
        try {
            return ResultUtil.success("成功", apiRouteUserService.findAll((JSONObject) JSONObject.toJSON(data)));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "新增Api开发者信息")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(@RequestBody @Valid ApiRouteUserInsertVm data, HttpServletRequest request) {
        try {
            apiRouteUserService.insert((JSONObject) JSONObject.toJSON(data), request);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "获取数据详细信息", notes = "根据id来获取详细信息")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result<ApiRouteUser> findById(String id) {
        ApiRouteUser mUser = apiRouteUserService.findById(id);
        return mUser != null ? ResultUtil.success("获取详细信息", mUser) : ResultUtil.fail("数据为空或获取失败!");
    }

    @ApiOperation(value = "删除数据", notes = "根据id来指定删除数据")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    public Result delete(String id) {
        try {
            apiRouteUserService.delete(id);
        } catch (Exception e) {
            return ResultUtil.fail("删除失败:" + e.getMessage());
        }
        return ResultUtil.success("删除成功!");
    }
}

@ApiModel(value = "ApiRouteUserInsertVm", description = "开发者管理添加")
class ApiRouteUserInsertVm {
    @ApiModelProperty(value = "集成项目id")
    private String integrationId;
    @ApiModelProperty(value = "系统服务id")
    @NotBlank(message = "系统服务id不能为空")
    private String sysServiceId;
    @ApiModelProperty(value = "开发者id")
    private String appid;
    @ApiModelProperty(value = "开发者密钥")
    private String appkey;
    @ApiModelProperty(value = "备注")
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

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "ApiRouteUserFindAllVm", description = "开发者管理查询")
class ApiRouteUserFindAllVm extends PageableVm {
    @ApiModelProperty(value = "集成项目id")
    @NotBlank(message = "集成项目id不能为空")
    private String integrationId;
    @ApiModelProperty(value = "系统服务id")
    @NotBlank(message = "系统服务id不能为空")
    private String sysServiceId;
    @ApiModelProperty(value = "开发者id")
    private String appid;

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

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}

@ApiModel(value = "ApiRouteUserSyncPutVm", description = "同步开发者信息")
class ApiRouteUserSyncPutVm {
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
