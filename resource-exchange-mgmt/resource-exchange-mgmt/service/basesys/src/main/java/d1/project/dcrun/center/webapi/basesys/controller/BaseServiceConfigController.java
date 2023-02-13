package d1.project.dcrun.center.webapi.basesys.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.basesys.model.CustomSysServiceConfigResultVm;
import d1.project.dcrun.center.webapi.basesys.service.BaseServiceConfigService;
import d1.project.dcrun.center.webapi.common.PageableVm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author xuaa
 */
@Auth("integration")
@RestController
@RequestMapping("/integration/custom/config")
@Api(value = "/integration/custom/config", tags = "webapi系统服务自定义配置")
public class BaseServiceConfigController {

    @Autowired
    private BaseServiceConfigService baseServiceConfigService;

    @ApiOperation(value = "自定义系统服务配置编辑", notes = "自定义系统服务配置编辑")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> updateCustomConfig(@Valid @RequestBody CustomSysServiceConfigUpdateVm model) {
        try {
            baseServiceConfigService.updateCustomConfig((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail("编辑失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查询自定义配置信息(列表)", notes = "查询自定义配置信息(列表)")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<CustomSysServiceConfigResultVm>> findAll(@Valid CustomSysServiceConfigGetVm model) {
        try {
            return ResultUtil.success("查询成功", baseServiceConfigService.findAll((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail("查询失败" + e.getMessage(), e);
        }
    }
}

@ApiModel(value = "CustomSysServiceConfigUpdateVm", description = "自定义系统服务配置")
class CustomSysServiceConfigUpdateVm {
    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id")
    private String integrationId;
    @NotBlank(message = "系统服务id不能为空")
    @ApiModelProperty(value = "系统服务id")
    private String appid;
    @ApiModelProperty(value = "配置项")
    private JSONObject data;

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}

@ApiModel(value = "CustomSysServiceConfigGetVm", description = "查询自定义配置")
class CustomSysServiceConfigGetVm extends PageableVm {
    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id")
    private String integrationId;
    @NotBlank(message = "系统服务appid不能为空")
    @ApiModelProperty(value = "系统服务id")
    private String appid;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }
}

