package d1.project.dcrun.center.webapi.emq.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.emq.service.EmqConfigService;
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
@RequestMapping("/integration/emq/config")
@Api(value = "/integration/emq/config", tags = "消息服务数据库配置")
public class EmqConfigController {

    @Autowired
    private EmqConfigService emqConfigService;

    @ApiOperation(value = "查询数据库配置信息(列表)", notes = "查询数据库配置信息(列表)")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<JSONObject> findAll(@Valid EmqConfigGetVm model) {
        try {
            return ResultUtil.success("查询成功", emqConfigService.findAll((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail("查询失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "修改数据库配置信息", notes = "修改数据库配置信息")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(@Valid @RequestBody EmqConfigUpdateVm model) {
        try {
            emqConfigService.update((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("保存成功");
        } catch (Exception e) {
            return ResultUtil.fail("保存失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "消息集成服务数据库配置更新", notes = "消息集成服务数据库配置更新")
    @RequestMapping(value = "/sync", method = {RequestMethod.POST})
    public Result<String> syncEmqConfig(@Valid @RequestBody EmqConfigGetVm model) {
        try {
            emqConfigService.syncEmqConfig((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("数据库配置更新成功");
        } catch (Exception e) {
            return ResultUtil.fail("数据库配置更新失败" + e.getMessage(), e);
        }
    }
}

@ApiModel(value = "EmqConfigGetVm", description = "查询数据库配置")
class EmqConfigGetVm {
    @NotBlank(message = "系统服务appid不能为空")
    @ApiModelProperty(value = "系统服务id")
    private String appid;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}

@ApiModel(value = "EmqConfigUpdateVm", description = "数据库配置")
class EmqConfigUpdateVm {
    @NotBlank(message = "系统服务id不能为空")
    @ApiModelProperty(value = "系统服务id")
    private String appid;
    @ApiModelProperty(value = "配置项")
    private JSONObject data;

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
