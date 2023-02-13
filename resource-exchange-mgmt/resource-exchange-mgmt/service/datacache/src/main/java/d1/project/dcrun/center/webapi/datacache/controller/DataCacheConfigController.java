package d1.project.dcrun.center.webapi.datacache.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.datacache.service.DataCacheConfigService;
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
@RequestMapping("/integration/datacache/config")
@Api(value = "/integration/datacache/config", tags = "数据缓存服务消息服务配置")
public class DataCacheConfigController {

    @Autowired
    private DataCacheConfigService dataCacheConfigService;

    @ApiOperation(value = "查询消息服务置信息(列表)", notes = "查询消息服务配置信息(列表)")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<JSONObject> findAll(@Valid DataCacheConfigGetVm model) {
        try {
            return ResultUtil.success("查询成功", dataCacheConfigService.findAll((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail("查询失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "修改数据库配置信息", notes = "修改数据库配置信息")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(@Valid @RequestBody DataCacheConfigUpdateVm model) {
        try {
            dataCacheConfigService.update((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("保存成功");
        } catch (Exception e) {
            return ResultUtil.fail("保存失败" + e.getMessage(), e);
        }
    }
}

@ApiModel(value = "DataCacheConfigGetVm", description = "查询消息服务配置")
class DataCacheConfigGetVm {
    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id")
    private String integrationId;
    @NotBlank(message = "系统服务appid不能为空")
    @ApiModelProperty(value = "系统服务id")
    private String appid;

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
}

@ApiModel(value = "DataCacheConfigUpdateVm", description = "数据库配置")
class DataCacheConfigUpdateVm {
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
