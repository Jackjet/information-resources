package d1.project.dcrun.center.webapi.system.controller.integration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.system.service.CoreSysServiceConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/integration/config")
@Api(value = "/integration/config", tags = "系统服务配置")
public class CoreSysServiceConfigController {

    @Autowired
    private CoreSysServiceConfigService coreSysServiceConfigService;

    @ApiOperation(value = "系统服务配置（列表）", notes = "查询系统服务配置")
    @RequestMapping(value = "/findAll", method = {RequestMethod.GET})
    public Result<JSONObject> findAll(@Valid SysServiceConfigFindAllVm model) {
        try {
            return ResultUtil.success("查询成功", coreSysServiceConfigService.findAllConfig((JSONObject) JSONObject.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail("查询失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "系统服务配置编辑", notes = "系统服务配置编辑")
    @RequestMapping(value = "/update", method = {RequestMethod.PUT})
    public Result<String> updateSysConfig(@Valid @RequestBody SysServiceConfigUpdateVm model, HttpServletRequest request) {
        try {
            coreSysServiceConfigService.updateSysConfig((JSONObject) JSONObject.toJSON(model), request);
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail("编辑失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "系统服务配置版本号", notes = "系统服务配置版本号")
    @RequestMapping(value = "/version", method = {RequestMethod.GET})
    public Result<String> version(@Valid SysServiceConfigVersionGetVm model) {
        try {
            return ResultUtil.success("查询成功", coreSysServiceConfigService.version((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail("查询失败" + e.getMessage(), e);
        }
    }
}

@ApiModel(value = "SysServiceConfigFindAllVm", description = "系统服务配置")
class SysServiceConfigFindAllVm {
    @NotBlank(message = "系统服务id不能为空")
    @ApiModelProperty(value = "系统服务id")
    private String appid;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}

@ApiModel(value = "sysServiceConfigUpdateVm", description = "系统服务编辑")
class SysServiceConfigUpdateVm {
    @NotBlank(message = "系统服务id不能为空")
    @ApiModelProperty(value = "系统服务id")
    private String appid;

    @ApiModelProperty(value = "选择的服务模板Id")
    private String templateId;

    @ApiModelProperty(value = "系统服务名称")
    private String name;

    @ApiModelProperty(value = "系统服务url")
    private String url;

    @ApiModelProperty(value = "服务端口(webapi及dcapigateway模板类型需要设置此值)")
    private String serverPort;

    @ApiModelProperty(value = "备注")
    private String remark;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "SysServiceConfigVersionGetVm", description = "系统服务配置版本号")
class SysServiceConfigVersionGetVm {
    @NotBlank(message = "系统服务id不能为空")
    @ApiModelProperty(value = "系统服务id")
    private String appid;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}

