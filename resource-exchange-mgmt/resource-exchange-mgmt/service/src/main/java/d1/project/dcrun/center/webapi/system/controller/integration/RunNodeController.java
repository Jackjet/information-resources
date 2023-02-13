package d1.project.dcrun.center.webapi.system.controller.integration;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.system.entity.RunNode;
import d1.project.dcrun.center.webapi.system.model.RunNodeTreeJsonVm;
import d1.project.dcrun.center.webapi.system.model.runnode.RunNodeVm;
import d1.project.dcrun.center.webapi.system.service.RunNodeService;
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
 * @author libin
 */
@Auth("integration")
@RestController
@RequestMapping("/integration/runNode")
@Api(value = "/integration/runNode", tags = "运行节点管理")
public class RunNodeController {
    @Autowired
    private RunNodeService runNodeService;

    @ApiOperation(value = "查询所有节点信息", notes = "查询所有节点信息")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<List<RunNodeTreeJsonVm>> findAll(RunNodeFindAllGetVm params) {
        try {
            List<RunNodeTreeJsonVm> result = this.runNodeService.findAllByIntegrationIdAndPath((JSONObject) JSONObject.toJSON(params));
            return ResultUtil.success("获取成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查询节点信息", notes = "查询节点信息")
    @RequestMapping(value = "/getById", method = RequestMethod.GET)
    public Result<RunNodeVm> getById(String id) {
        try {
            RunNodeVm result = this.runNodeService.getById(id);
            return ResultUtil.success("获取成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查询节点基础服务是否在线")
    @RequestMapping(value = "/isOnline", method = RequestMethod.GET)
    public Result<Boolean> isOnline(@Valid RunNodeGetVm model) {
        try {
            return ResultUtil.success("获取成功", runNodeService.isOnline((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "新增节点信息", notes = "新增节点信息")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(HttpServletRequest request, @RequestBody @Valid RunNodeInsertPostVm model) {
        try {
            JSONObject jo = (JSONObject) JSONObject.toJSON(model);
            this.runNodeService.insert(request, jo);
            return ResultUtil.success("创建成功");
        } catch (Exception e) {
            return ResultUtil.fail("创建失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "编辑节点信息", notes = "编辑节点信息")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(HttpServletRequest request, @RequestBody @Valid RunNodeUpdatePutVm model) {
        try {
            JSONObject jo = (JSONObject) JSONObject.toJSON(model);
            this.runNodeService.update(request, jo);
            return ResultUtil.success("修改成功");
        } catch (Exception e) {
            return ResultUtil.fail("修改失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "获取数据详细信息", notes = "根据id来获取详细信息")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result<RunNode> findById(String id) {
        RunNode mUser = runNodeService.findById(id);
        return mUser != null ? ResultUtil.success("获取详细信息", mUser) : ResultUtil.fail("数据为空或获取失败!");
    }


    @ApiOperation(value = "删除数据", notes = "根据id来指定删除数据")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    public Result delete(String id) {
        try {
            runNodeService.delete(id);
        } catch (Exception e) {
            return ResultUtil.fail("删除失败:" + e.getMessage());
        }
        return ResultUtil.success("删除成功!");
    }
}

@ApiModel(value = "RunNodeInsertPostVm", description = "新增节点信息")
class RunNodeInsertPostVm {
    @ApiModelProperty(value = "集成项目id")
    @NotBlank(message = "集成项目id不能为空")
    private String integrationId;

    @ApiModelProperty(value = "运行节点名称")
    private String name;

    @ApiModelProperty(value = "父节点id")
    private String parentNodeId;

    @ApiModelProperty(value = "备注")
    private String remark;

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentNodeId() {
        return parentNodeId;
    }

    public void setParentNodeId(String parentNodeId) {
        this.parentNodeId = parentNodeId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "RunNodeUpdatePutVm", description = "编辑节点信息")
class RunNodeUpdatePutVm {
    @ApiModelProperty(value = "节点id")
    @NotBlank(message = "节点id不能为空")
    private String id;

    @ApiModelProperty(value = "备注")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "RunNodeGetVm", description = "判断节点是否在线")
class RunNodeGetVm {
    @ApiModelProperty(value = "节点id")
    @NotBlank(message = "节点id不能为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

@ApiModel(value = "RunNodeFindAllGetVm", description = "查询节点信息")
class RunNodeFindAllGetVm {
    @ApiModelProperty(value = "集成项目id")
    @NotBlank(message = "集成项目id不能为空")
    private String integrationId;
    private String name;

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
