package d1.project.dcrun.center.webapi.datacache.controller;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.dcrun.center.webapi.datacache.entity.KeyConfig;
import d1.project.dcrun.center.webapi.datacache.service.KeyConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author maoyy
 */
@Auth("integration")
@RestController
@RequestMapping("/integration/key/config")
@Api(value = "/integration/key/config", tags = "key配置信息")
public class KeyConfigController {
    @Autowired
    private KeyConfigService keyConfigService;

    @ApiOperation(value = "查询key配置信息", notes = "查询key配置信息列表")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<KeyConfig>> findAll(@Valid KeyConfigFindAllVm model) {
        try {
            return ResultUtil.success("获取成功", keyConfigService.findAll((JSONObject) JSONObject.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "key配置信息添加", notes = "key配置信息添加")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(HttpServletRequest request, @Valid @RequestBody KeyConfigInsertVm model) {
        try {
            keyConfigService.insert(request, (JSONObject) JSONObject.toJSON(model));
            return ResultUtil.success("创建成功");
        } catch (Exception e) {
            return ResultUtil.fail("创建失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除key配置信息")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public Result<String> delete(@PathVariable("id") String id) {
        try {
            keyConfigService.deleteById(id);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail("删除失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "同步配置信息，生成路由数据json文件，发送MQTT至微应用，同步配置信息")
    @RequestMapping(value = "/syncKeys", method = RequestMethod.PUT)
    public Result<String> syncKeys(@RequestBody @Valid SyncKeys model) {
        try {
            JSONObject jo = (JSONObject) JSONObject.toJSON(model);
            keyConfigService.syncKeys(jo);
            return ResultUtil.success("同步成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
}

@ApiModel(value = "KeyConfigFindAllVm", description = "查询key配置信息")
class KeyConfigFindAllVm extends PageableVm {
    @NotBlank(message = "集成项目id不能为空")
    @ApiModelProperty(value = "集成项目id")
    private String integrationId;
    @NotBlank(message = "系统服务id不能为空")
    @ApiModelProperty(value = "系统服务id")
    private String sysServiceId;
    @NotBlank(message = "key不能为空")
    @ApiModelProperty(value = "key")
    private String key;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

@ApiModel(value = "KeyConfigInsertVm", description = "添加或修改key配置信息")
class KeyConfigInsertVm {
    @NotBlank(message = "集成项目id不能为空")
    @ApiModelProperty(value = "集成项目id")
    private String integrationId;
    @NotBlank(message = "系统服务id不能为空")
    @ApiModelProperty(value = "系统服务id")
    private String sysServiceId;
    @NotBlank(message = "key不能为空")
    @ApiModelProperty(value = "key")
    private String key;
    @ApiModelProperty(value = "expire")
    private Integer expire;
    @ApiModelProperty(value = "data [{appid”:”开发者id”,permission”:”0”}")
    private List<KeyConfigDataVm> data;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }

    public List<KeyConfigDataVm> getData() {
        return data;
    }

    public void setData(List<KeyConfigDataVm> data) {
        this.data = data;
    }
}

class KeyConfigDataVm {
    @NotBlank(message = "开发者id不能为空")
    @ApiModelProperty(value = "开发者id")
    private String appid;
    @ApiModelProperty(value = "开发者名称")
    private String name;
    @NotBlank(message = "权限不能为空")
    @ApiModelProperty(value = "0:读 1:写 2:读+写")
    private String permission;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}

@ApiModel(value = "SyncKeys", description = "api同步")
class SyncKeys {
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

