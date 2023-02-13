package d1.project.tangshan.operation.manage.controller.webadmin.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.tangshan.operation.manage.entity.remote.CommandType;
import d1.project.tangshan.operation.manage.entity.remote.Commands;
import d1.project.tangshan.operation.manage.service.remote.CommandTypeService;
import d1.project.tangshan.operation.manage.service.remote.CommandsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author lin
 */
@Auth("webadmin")
@RestController("/webadmin/remoteControl/commands")
@RequestMapping("/webadmin/remoteControl/commands")
@Api(value = "/webadmin/remoteControl/commands", tags = "日常运维指令管理")
public class CommandsController {
    private final CommandsService commandsService;
    private final CommandTypeService commandTypeService;

    public CommandsController(CommandsService commandsService, CommandTypeService commandTypeService) {
        this.commandsService = commandsService;
        this.commandTypeService = commandTypeService;
    }


    @ApiOperation(value = "新增")
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody CommandsInsertVm model, HttpServletRequest request) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            commandsService.insert(jsonObject, request);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除日常运维指令")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) {
        try {
            commandsService.delete(id, request);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "日常运维指令编辑")
    @PutMapping(value = "/update")
    public Result<String> update(HttpServletRequest request, @Valid @RequestBody CommandsUpdateVm model) {
        try {
            commandsService.update(request, (JSONObject) JSON.toJSON(model));
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "日常运维指令列表（分页）")
    @GetMapping(value = "/findAll")
    public Result<Page<Commands>> findAll(CommandsFindAllVm model) {
        try {
            Page<Commands> result = commandsService.findAll((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<Commands> findById(String id) {
        try {
            return ResultUtil.success("成功", commandsService.findById(id));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查询所有指令分类")
    @GetMapping(value = "/findCommandTypes")
    public Result<List<CommandType>> findCommandTypes() {
        try {
            List<CommandType> result = commandTypeService.findAllCommandTypes();
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
}

@ApiModel(value = "CommandsInsertVm", description = "新增")
class CommandsInsertVm {
    @ApiModelProperty("类型id")
    private String typeId;

    @ApiModelProperty("类型名称")
    private String type;

    @ApiModelProperty("指令内容")
    private String content;

    @ApiModelProperty("备注")
    private String remark;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "CommandsUpdateVm", description = "编辑")
class CommandsUpdateVm {

    @ApiModelProperty("指令id")
    @NotBlank(message = "指令id不可为空")
    private String id;

    @ApiModelProperty("类型id")
    private String typeId;

    @ApiModelProperty("类型名称")
    private String type;

    @ApiModelProperty("指令内容")
    private String content;

    @ApiModelProperty("备注")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "CommandsFindAllVm", description = "查询")
class CommandsFindAllVm extends PageableVm {
    @ApiModelProperty(value = "类型id")
    private String typeId;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}