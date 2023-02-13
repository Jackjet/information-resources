package d1.project.dcrun.center.webapi.dcapi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.common.service.system.SysServiceService;
import d1.project.dcrun.center.webapi.dcapi.service.ApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author maoyy
 */
@Auth("integration")
@RestController
@RequestMapping("/integration/api")
@Api(value = "/integration/api", tags = "Api集成服务")
public class ApiController {
    @Autowired
    private SysServiceService sysServiceService;

    @Autowired
    private ApiService apiService;

    @ApiOperation(value = "API集成服务启动", notes = "API集成服务启动")
    @RequestMapping(value = "/start", method = {RequestMethod.PUT})
    public Result<String> start(@Valid @RequestBody ApiStartAndStopVm model) {
        try {
            sysServiceService.start(model.getId());
            return ResultUtil.success("启动成功");
        } catch (Exception e) {
            return ResultUtil.fail("启动失败" + e.getMessage());
        }
    }

    @ApiOperation(value = "API集成服务停止", notes = "API集成服务停止")
    @RequestMapping(value = "/stop", method = {RequestMethod.PUT})
    public Result<String> stop(@Valid @RequestBody ApiStartAndStopVm model) {
        try {
            sysServiceService.stop(model.getId());
            return ResultUtil.success("停止成功");
        } catch (Exception e) {
            return ResultUtil.fail("停止失败" + e.getMessage());
        }
    }

    @ApiOperation(value = "API集成服务安装", notes = "API集成服务安装")
    @RequestMapping(value = "/install", method = {RequestMethod.POST})
    public Result<String> install(@Valid @RequestBody ApiInstallVm model) {
        try {
            apiService.install((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("安装成功");
        } catch (Exception e) {
            return ResultUtil.fail("安装失败" + e.getMessage());
        }
    }
}

@ApiModel(value = "ApiInstallVm", description = "数据库配置")
class ApiInstallVm {
    @NotBlank(message = "系统服务appid不能为空")
    @ApiModelProperty(value = "系统服务appid")
    private String appid;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}

@ApiModel(value = "ApiStartAndStopVm", description = "数据库配置")
class ApiStartAndStopVm {
    @NotBlank(message = "系统服务appid不能为空")
    @ApiModelProperty(value = "系统服务appid")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
