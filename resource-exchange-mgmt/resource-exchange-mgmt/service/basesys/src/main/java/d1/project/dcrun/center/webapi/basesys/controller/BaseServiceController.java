package d1.project.dcrun.center.webapi.basesys.controller;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.common.service.system.SysServiceService;
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
 * @author xuaa
 */
@Auth("integration")
@RestController
@RequestMapping("/integration/base")
@Api(value = "/integration/base", tags = "webapi系统基础服务")
public class BaseServiceController {

    @Autowired
    private SysServiceService sysServiceService;

    @ApiOperation(value = "webapi系统服务启动")
    @RequestMapping(value = "/start", method = {RequestMethod.PUT})
    public Result<String> start(@Valid @RequestBody IdVm model) {
        try {
            sysServiceService.start(model.getId());
            return ResultUtil.success("启动成功");
        } catch (Exception e) {
            return ResultUtil.fail("启动失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "webapi系统服务停止")
    @RequestMapping(value = "/stop", method = {RequestMethod.PUT})
    public Result<String> stop(@Valid @RequestBody IdVm model) {
        try {
            sysServiceService.stop(model.getId());
            return ResultUtil.success("停止成功");
        } catch (Exception e) {
            return ResultUtil.fail("停止失败" + e.getMessage(), e);
        }
    }
}

@ApiModel(value = "IdVm", description = "数据库配置")
class IdVm {
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

