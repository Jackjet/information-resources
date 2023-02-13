package d1.project.dcrun.center.webapi.emq.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.common.service.system.SysServiceService;
import d1.project.dcrun.center.webapi.emq.service.EmqSysService;
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
@RequestMapping("/integration/emq")
@Api(value = "/integration/emq", tags = "消息集成服务")
public class EmqController {

    @Autowired
    private SysServiceService sysServiceService;

    @Autowired
    private EmqSysService emqSysService;

    @ApiOperation(value = "消息集成服务启动", notes = "消息集成服务启动")
    @RequestMapping(value = "/start", method = {RequestMethod.PUT})
    public Result<String> start(@Valid @RequestBody EmqStartAndStopVm model) {
        try {
            sysServiceService.start(model.getId());
            return ResultUtil.success("启动成功");
        } catch (Exception e) {
            return ResultUtil.fail("启动失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "消息集成服务停止", notes = "消息集成服务停止")
    @RequestMapping(value = "/stop", method = {RequestMethod.PUT})
    public Result<String> stop(@Valid @RequestBody EmqStartAndStopVm model) {
        try {
            sysServiceService.stop(model.getId());
            return ResultUtil.success("停止成功");
        } catch (Exception e) {
            return ResultUtil.fail("停止失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "消息集成服务安装", notes = "消息集成服务安装")
    @RequestMapping(value = "/install", method = {RequestMethod.POST})
    public Result<String> install(@Valid @RequestBody EmqInstallVm model) {
        try {
            emqSysService.install((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("安装成功");
        } catch (Exception e) {
            return ResultUtil.fail("安装失败" + e.getMessage(), e);
        }
    }

}

@ApiModel(value = "EmqInstallVm", description = "数据库配置")
class EmqInstallVm {
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

@ApiModel(value = "EmqStartAndStopVm", description = "数据库配置")
class EmqStartAndStopVm {
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
