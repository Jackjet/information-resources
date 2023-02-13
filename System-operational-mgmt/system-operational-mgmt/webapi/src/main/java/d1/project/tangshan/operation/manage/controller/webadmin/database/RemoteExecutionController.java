package d1.project.tangshan.operation.manage.controller.webadmin.database;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.tangshan.operation.manage.entity.database.Instances;
import d1.project.tangshan.operation.manage.entity.operations.module.Database;
import d1.project.tangshan.operation.manage.entity.operations.module.Node;
import d1.project.tangshan.operation.manage.service.database.InstancesService;
import d1.project.tangshan.operation.manage.service.database.RemoteExecutionService;
import d1.project.tangshan.operation.manage.service.operations.module.DatabaseService;
import d1.project.tangshan.operation.manage.service.operations.module.NodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author lin
 */
@Auth("webadmin")
@RestController("/webadmin/databaseOperation/remoteExecution")
@RequestMapping("/webadmin/databaseOperation/remoteExecution")
@Api(value = "/webadmin/databaseOperation/remoteExecution", tags = "远程sql管理")
public class RemoteExecutionController {
    private final RemoteExecutionService remoteExecutionService;
    private final InstancesService instancesService;
    private final NodeService nodeService;
    private final DatabaseService databaseService;

    public RemoteExecutionController(RemoteExecutionService remoteExecutionService, InstancesService instancesService, NodeService nodeService, DatabaseService databaseService) {
        this.remoteExecutionService = remoteExecutionService;
        this.instancesService = instancesService;
        this.nodeService = nodeService;
        this.databaseService = databaseService;
    }
    @ApiOperation(value = "执行")
    @PostMapping(value = "/sql")
    public Result<String> execute(@Valid @RequestBody SqlVm model, HttpServletRequest request) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);

            return ResultUtil.success("执行成功",remoteExecutionService.execute(jsonObject, request));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "节点列表")
    @GetMapping(value = "/findAllNodes")
    public Result<List<Node>> findAllNodes() {
        try {
            List<Node> result = nodeService.findAllNodes();
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "数据库列表")
    @GetMapping(value = "/findAllDatabases")
    public Result<List<Database>> findAllDatabases(String nodeId) {
        try {
            List<Database> result = databaseService.findAllByNodeId(nodeId);
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
    @ApiOperation(value = "实例列表")
    @GetMapping(value = "/findAllInstances")
    public Result<List<Instances>> findAllInstances(String nodeId, String databaseId) {
        try {
            List<Instances> result = instancesService.findAllByNodeIdAndDatabaseId(nodeId,databaseId);
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
}
@ApiModel(value = "SqlVm", description = "sqlvm")
class SqlVm {
    @ApiModelProperty("节点id")
    @NotBlank(message = "节点名称不可为空")
    private String nodeId;
    @ApiModelProperty("数据库id")
    @NotBlank(message = "数据库名称不可为空")
    private String databaseId;
    @ApiModelProperty("sql语句")
    @NotBlank(message = "SQL不可为空")
    private String content;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}