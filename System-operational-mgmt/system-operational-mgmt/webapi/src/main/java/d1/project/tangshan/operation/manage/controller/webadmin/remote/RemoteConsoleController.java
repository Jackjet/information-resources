package d1.project.tangshan.operation.manage.controller.webadmin.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.tangshan.operation.manage.entity.remote.CommandType;
import d1.project.tangshan.operation.manage.entity.remote.Commands;
import d1.project.tangshan.operation.manage.service.remote.RemoteConsoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author lin
 */
@Auth("webadmin")
@RestController("/webadmin/remoteControl/remoteConsole")
@RequestMapping("/webadmin/remoteControl/remoteConsole")
@Api(value = "/webadmin/remoteControl/remoteConsole", tags = "远程控制台管理")
public class RemoteConsoleController {
    private final RemoteConsoleService remoteConsoleService;

    public RemoteConsoleController(RemoteConsoleService remoteConsoleService) {
        this.remoteConsoleService = remoteConsoleService;
    }

    @ApiOperation(value = "远程控制台检查权限")
    @GetMapping(value = "/checkPermissions")
    public Result<String> checkPermissions(String typeId, HttpServletRequest request) {
        try {
            remoteConsoleService.checkPermissions(typeId, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查询所有指令")
    @GetMapping(value = "/findCommands")
    public Result<Page<Commands>> findCommands(String content) {
        try {
            return ResultUtil.success("成功", remoteConsoleService.findCommands(content));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "执行")
    @PostMapping(value = "/execute")
    public Result<String> execute(@Valid @RequestBody ExecuteVm model, HttpServletRequest request) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            remoteConsoleService.execute(jsonObject, request);
            return ResultUtil.success("执行成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
}
@ApiModel(value = "ExecuteVm", description = "执行")
class ExecuteVm {
    @ApiModelProperty("节点id")
    @NotBlank(message = "节点id不可为空")
    private String nodeId;
    @ApiModelProperty("指令")
    @NotBlank(message = "节指令不可为空")
    private String command;
    @ApiModelProperty("日常运维指令id")
    private String commandsId;
    @ApiModelProperty("是否有权限")
    private String isPermissions;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommandsId() {
        return commandsId;
    }

    public void setCommandsId(String commandsId) {
        this.commandsId = commandsId;
    }

    public String getIsPermissions() {
        return isPermissions;
    }

    public void setIsPermissions(String isPermissions) {
        this.isPermissions = isPermissions;
    }
}