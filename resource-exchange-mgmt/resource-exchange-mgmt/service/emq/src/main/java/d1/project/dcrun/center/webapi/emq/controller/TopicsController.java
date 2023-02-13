package d1.project.dcrun.center.webapi.emq.controller;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.common.PageableVm;
import d1.project.dcrun.center.webapi.emq.entity.Topics;
import d1.project.dcrun.center.webapi.emq.service.TopicsService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("/integration/topics")
@Api(value = "/integration/topics", tags = "Topic信息")
public class TopicsController {

    @Autowired
    private TopicsService topicService;

    @ApiOperation(value = "查询Topic信息", notes = "查询Topic信息列表")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<Topics>> findAll(@Valid TopicFindAllVm model) {
        try {
            return ResultUtil.success("获取成功", this.topicService.findAll((JSONObject) JSONObject.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "Topic信息添加", notes = "Topic信息添加")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(HttpServletRequest request, @Valid @RequestBody TopicInsertVm model) {
        try {
            this.topicService.insert(request, (JSONObject) JSONObject.toJSON(model));
            return ResultUtil.success("创建成功");
        } catch (Exception e) {
            return ResultUtil.fail("创建失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "Topic信息删除", notes = "Topic信息删除")
    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    public Result<String> delete(String id) {
        try {
            this.topicService.deleteById(id);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail("删除失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "获取数据详细信息", notes = "根据id来获取详细信息")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result<Topics> findById(String id) {
        Topics mUser = topicService.findById(id);
        return mUser != null ? ResultUtil.success("获取详细信息", mUser) : ResultUtil.fail("数据为空或获取失败!");
    }
}

@ApiModel(value = "TopicFindAllVm", description = "查询Topic信息")
class TopicFindAllVm extends PageableVm {
    @NotBlank(message = "集成项目id不能为空")
    @ApiModelProperty(value = "集成项目id")
    private String integrationId;
    @NotBlank(message = "系统服务id不能为空")
    @ApiModelProperty(value = "系统服务id")
    private String sysServiceId;
    @ApiModelProperty(value = "Topic名称")
    private String topicName;

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

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}

@ApiModel(value = "TopicInsertVm", description = "添加")
class TopicInsertVm {
    @NotBlank(message = "集成项目id不能为空")
    @ApiModelProperty(value = "集成项目id")
    private String integrationId;
    @NotBlank(message = "系统服务id不能为空")
    @ApiModelProperty(value = "系统服务id")
    private String sysServiceId;
    @NotBlank(message = "Topic名称不能为空")
    @ApiModelProperty(value = "Topic名称")
    private String topicName;
    @ApiModelProperty(value = "备注")
    private String remark;

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

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
