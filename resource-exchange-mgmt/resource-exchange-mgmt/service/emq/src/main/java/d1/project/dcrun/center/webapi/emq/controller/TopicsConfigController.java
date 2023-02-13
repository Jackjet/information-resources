package d1.project.dcrun.center.webapi.emq.controller;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.emq.entity.TopicConfig;
import d1.project.dcrun.center.webapi.emq.model.TopicConfigFindAllResVm;
import d1.project.dcrun.center.webapi.emq.service.TopicConfigService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author xuaa
 */
@Auth("integration")
@RestController
@RequestMapping("/integration/topicConfig")
@Api(value = "/integration/topicConfig", tags = "Topic访问控制权限")
public class TopicsConfigController {

    @Autowired
    private TopicConfigService topicConfigService;

    @ApiOperation(value = "查询开发者Topic访问控制权限", notes = "查询开发者Topic访问控制权限")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<List<TopicConfigFindAllResVm>> findAll(@Valid TopicConfigFindAllVm model) {
        try {
            return ResultUtil.success("获取成功", this.topicConfigService.findAll((JSONObject) JSONObject.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "新增开发者Topic访问控制权限", notes = "新增开发者Topic访问控制权限")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(HttpServletRequest request, @Valid @RequestBody TopicConfigInsertVm model) {
        try {
            this.topicConfigService.insert(request, (JSONObject) JSONObject.toJSON(model));
            return ResultUtil.success("保存成功");
        } catch (Exception e) {
            return ResultUtil.fail("保存失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "同步开发者Topic访问控制权限", notes = "同步开发者Topic访问控制权限")
    @RequestMapping(value = "/syncTopicPermission", method = RequestMethod.POST)
    public Result<String> syncTopicPermission(@Valid @RequestBody SyncTopicPermissionVm model) {
        try {
            this.topicConfigService.syncTopicPermission((JSONObject) JSONObject.toJSON(model));
            return ResultUtil.success("同步成功");
        } catch (Exception e) {
            return ResultUtil.fail("同步失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "获取数据详细信息", notes = "根据id来获取详细信息")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result<TopicConfig> findById(String id) {
        TopicConfig mUser = topicConfigService.findById(id);
        return mUser != null ? ResultUtil.success("获取详细信息", mUser) : ResultUtil.fail("数据为空或获取失败!");
    }


    @ApiOperation(value = "删除数据", notes = "根据id来指定删除数据")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    public Result delete(String id) {
        try {
            topicConfigService.delete(id);
        } catch (Exception e) {
            return ResultUtil.fail("删除失败:" + e.getMessage());
        }
        return ResultUtil.success("删除成功!");
    }
}

@ApiModel(value = "TopicConfigFindAllVm", description = "查询开发者Topic访问控制权限")
class TopicConfigFindAllVm {
    @NotBlank(message = "集成项目id不能为空")
    @ApiModelProperty(value = "集成项目id")
    private String integrationId;
    @NotBlank(message = "系统服务id不能为空")
    @ApiModelProperty(value = "系统服务id")
    private String sysServiceId;
    @NotBlank(message = "topicId不能为空")
    @ApiModelProperty(value = "topicId")
    private String topicId;
    @ApiModelProperty(value = "appid")
    private String appid;

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

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}

@ApiModel(value = "SyncTopicPermissionVm", description = "同步开发者信息")
class SyncTopicPermissionVm {
    @NotBlank(message = "集成项目id不能为空")
    @ApiModelProperty(value = "集成项目id")
    private String integrationId;
    @NotBlank(message = "系统服务id不能为空")
    @ApiModelProperty(value = "系统服务id")
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

@ApiModel(value = "TopicConfigInsertVm", description = "新增开发者Topic访问控制权限")
class TopicConfigInsertVm {
    @NotBlank(message = "集成项目id不能为空")
    @ApiModelProperty(value = "集成项目id")
    private String integrationId;
    @NotBlank(message = "系统服务id不能为空")
    @ApiModelProperty(value = "系统服务id")
    private String sysServiceId;
    @NotBlank(message = "topicId不能为空")
    @ApiModelProperty(value = "topicId")
    private String topicId;
    @ApiModelProperty(value = "data [{appid”:”开发者id”,permission”:”0”}")
    private List<TopicConfigInsertResVm> data;

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

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public List<TopicConfigInsertResVm> getData() {
        return data;
    }

    public void setData(List<TopicConfigInsertResVm> data) {
        this.data = data;
    }
}

class TopicConfigInsertResVm {
    @ApiModelProperty(value = "开发者id")
    private String appid;
    @ApiModelProperty(value = "开发者名称")
    private String name;
    @ApiModelProperty(value = "权限：-1:删除事件权限数据 0:订阅 1:推送 2:订阅+推送")
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
