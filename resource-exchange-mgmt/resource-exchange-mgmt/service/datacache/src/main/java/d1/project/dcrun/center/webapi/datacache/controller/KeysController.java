package d1.project.dcrun.center.webapi.datacache.controller;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.common.PageableVm;
import d1.project.dcrun.center.webapi.datacache.entity.Keys;
import d1.project.dcrun.center.webapi.datacache.service.KeysService;
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

/**
 * @author maoyy
 */
@Auth("integration")
@RestController
@RequestMapping("/integration/keys")
@Api(value = "/integration/keys", tags = "keys信息")
public class KeysController {
    @Autowired
    private KeysService keysService;

    @ApiOperation(value = "查询key信息", notes = "查询key信息列表")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<Keys>> findAll(@Valid KeyFindAllVm model) {
        try {
            return ResultUtil.success("获取成功", keysService.findAll((JSONObject) JSONObject.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查询key详细信息", notes = "查询key详细信息")
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result<Page<Keys>> findById(String id) {
        try {
            return ResultUtil.success("获取详细信息成功", keysService.findById(id));
        } catch (Exception e) {
            return ResultUtil.fail("获取详细信息失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "key信息添加", notes = "key信息添加")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(HttpServletRequest request, @Valid @RequestBody KeyInsertVm model) {
        try {
            keysService.insert((JSONObject) JSONObject.toJSON(model), request);
            return ResultUtil.success("创建成功");
        } catch (Exception e) {
            return ResultUtil.fail("创建失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "key信息修改", notes = "key信息修改")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(HttpServletRequest request, @Valid @RequestBody KeyUpdateVm model) {
        try {
            keysService.update((JSONObject) JSONObject.toJSON(model), request);
            return ResultUtil.success("修改成功");
        } catch (Exception e) {
            return ResultUtil.fail("修改失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除key信息")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public Result<String> delete(@PathVariable("id") String id) {
        try {
            keysService.deleteById(id);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail("删除失败" + e.getMessage(), e);
        }
    }
}

@ApiModel(value = "KeyFindAllVm", description = "查询Keys信息")
class KeyFindAllVm extends PageableVm {
    @NotBlank(message = "集成项目id不能为空")
    @ApiModelProperty(value = "集成项目id")
    private String integrationId;
    @NotBlank(message = "系统服务id不能为空")
    @ApiModelProperty(value = "系统服务id")
    private String sysServiceId;
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

@ApiModel(value = "KeyInsertVm", description = "添加")
class KeyInsertVm {
    @NotBlank(message = "集成项目id不能为空")
    @ApiModelProperty(value = "集成项目id")
    private String integrationId;
    @NotBlank(message = "系统服务id不能为空")
    @ApiModelProperty(value = "系统服务id")
    private String sysServiceId;
    @NotBlank(message = "key不能为空")
    @ApiModelProperty(value = "key")
    private String key;
    @ApiModelProperty(value = "value示例")
    private String value;
    @ApiModelProperty("过期时间，秒，默认0(不限制")
    private Integer expire;

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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }
}

@ApiModel(value = "KeyUpdateVm", description = "添加")
class KeyUpdateVm {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private String id;
    @NotBlank(message = "key不能为空")
    @ApiModelProperty(value = "key")
    private String key;
    @ApiModelProperty(value = "value示例")
    private String value;
    @ApiModelProperty("过期时间，秒，默认0(不限制")
    private Integer expire;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getExpire() {
        return expire;
    }

    public void setExpire(Integer expire) {
        this.expire = expire;
    }
}
