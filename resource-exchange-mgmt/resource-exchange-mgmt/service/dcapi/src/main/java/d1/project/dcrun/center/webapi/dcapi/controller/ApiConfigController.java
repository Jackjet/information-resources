package d1.project.dcrun.center.webapi.dcapi.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.dcapi.service.ApiConfigService;
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
@RequestMapping("/integration/api/config")
@Api(value = "/integration/api/config", tags = "Api集成服务数据库配置")
public class ApiConfigController {
    @Autowired
    private ApiConfigService apiConfigService;

    @ApiOperation(value = "查询数据库配置信息(列表)", notes = "查询数据库配置信息(列表)")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Object> findAll(@Valid ApiConfigGetVm model) {
        try {
            return ResultUtil.success("查询成功", apiConfigService.findAll((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail("查询失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "修改数据库配置信息(列表)", notes = "修改数据库配置信息(列表)")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(@Valid @RequestBody ApiConfigUpdateVm model) {
        try {
            apiConfigService.update((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail("编辑失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "同步数据库配置信息(列表)", notes = "同步数据库配置信息(列表)")
    @RequestMapping(value = "/sync", method = RequestMethod.POST)
    public Result<String> sync(@Valid @RequestBody ApiConfigGetVm model) {
        try {
            apiConfigService.sync((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("同步成功");
        } catch (Exception e) {
            return ResultUtil.fail("同步失败" + e.getMessage(), e);
        }
    }

}

@ApiModel(value = "ApiConfigGetVm", description = "查询及同步数据库配置vm")
class ApiConfigGetVm {
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

@ApiModel(value = "ApiConfigUpdateVm", description = "编辑数据库配置vm")
class ApiConfigUpdateVm {
    @NotBlank(message = "系统服务id不能为空")
    @ApiModelProperty(value = "系统服务id")
    private String appid;
    @NotBlank(message = "数据库url不能为空")
    @ApiModelProperty(value = "数据库url")
    private String url;
    @NotBlank(message = "数据库用户名不能为空")
    @ApiModelProperty(value = "数据库用户名")
    private String username;
    @NotBlank(message = "数据库密码不能为空")
    @ApiModelProperty(value = "系数据库密码")
    private String password;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
