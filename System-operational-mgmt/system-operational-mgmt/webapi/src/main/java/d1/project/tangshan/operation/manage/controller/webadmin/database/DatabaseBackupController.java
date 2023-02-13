package d1.project.tangshan.operation.manage.controller.webadmin.database;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.tangshan.operation.manage.entity.database.DatabaseBackup;
import d1.project.tangshan.operation.manage.entity.database.Instances;
import d1.project.tangshan.operation.manage.entity.operations.module.Database;
import d1.project.tangshan.operation.manage.entity.operations.module.Node;
import d1.project.tangshan.operation.manage.service.database.DatabaseBackupService;
import d1.project.tangshan.operation.manage.service.database.InstancesService;
import d1.project.tangshan.operation.manage.service.operations.module.DatabaseService;
import d1.project.tangshan.operation.manage.service.operations.module.NodeService;
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
@RestController("/webadmin/databaseOperation/databaseBackup")
@RequestMapping("/webadmin/databaseOperation/databaseBackup")
@Api(value = "/webadmin/databaseOperation/databaseBackup", tags = "备份和恢复管理")
public class DatabaseBackupController {
    private final DatabaseBackupService databaseBackupService;
    private final NodeService nodeService;
    private final DatabaseService databaseService;
    private final InstancesService instancesService;

    public DatabaseBackupController(DatabaseBackupService databaseBackupService, NodeService nodeService, DatabaseService databaseService, InstancesService instancesService) {
        this.databaseBackupService = databaseBackupService;
        this.nodeService = nodeService;
        this.databaseService = databaseService;
        this.instancesService = instancesService;
    }


    @ApiOperation(value = "新增")
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody DatabaseBackupInsertVm model, HttpServletRequest request) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            databaseBackupService.insert(jsonObject, request);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除备份和恢复")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) {
        try {
            databaseBackupService.delete(id, request);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "恢复")
    @PostMapping(value = "/rollback")
    public Result<String> rollback(HttpServletRequest request, @Valid @RequestBody DatabaseBackupRecoverVm model) {
        try {
            databaseBackupService.recover(request, model.getId());
            return ResultUtil.success("恢复成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "参数配置列表（分页）")
    @GetMapping(value = "/findAll")
    public Result<Page<DatabaseBackup>> findAll(DatabaseBackupFindAllVm model) {
        try {
            Page<DatabaseBackup> result = databaseBackupService.findAll((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<DatabaseBackup> findById(String id) {
        try {
            return ResultUtil.success("成功", databaseBackupService.findById(id));
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
            List<Instances> result = instancesService.findAllByNodeIdAndDatabaseId(nodeId, databaseId);
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @Auth("noauth")
    @ApiOperation(value = "回调")
    @PostMapping(value = "/callback")
    public Result<String> callback(@Valid @RequestBody DatabaseBackupCallbackVm model) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            databaseBackupService.callback(jsonObject);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
}

@ApiModel(value = "DatabaseBackupInsertVm", description = "新增")
class DatabaseBackupInsertVm {
    @ApiModelProperty("节点id")
    @NotBlank(message = "节点id不可为空")
    private String nodeId;
    @ApiModelProperty("节点名称")
    @NotBlank(message = "节点名称不可为空")
    private String nodeName;
    @ApiModelProperty("数据库id")
    @NotBlank(message = "数据库id不可为空")
    private String databaseId;
    @ApiModelProperty("数据库名称")
    @NotBlank(message = "数据库名称不可为空")
    private String databaseName;
    @ApiModelProperty("实例Id")
    private String instanceId;
    @ApiModelProperty("实例名称")
    private String instanceName;
    @ApiModelProperty("备份位置")
    private String location;
    @ApiModelProperty("备注")
    private String remark;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "DatabaseBackupCallbackVm", description = "回调")
class DatabaseBackupCallbackVm {

    @ApiModelProperty("备份id")
    private String id;
    @ApiModelProperty("备份id")
    private String location;
    @ApiModelProperty("备份id")
    private String result;
    @ApiModelProperty("备份id")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "DatabaseBackupFindAllVm", description = "查询")
class DatabaseBackupFindAllVm extends PageableVm {
    @ApiModelProperty(value = "实例id")
    private String instancesId;
    @ApiModelProperty("节点id")
    private String nodeId;
    @ApiModelProperty("数据库id")
    private String databaseId;

    public String getInstancesId() {
        return instancesId;
    }

    public void setInstancesId(String instancesId) {
        this.instancesId = instancesId;
    }

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
}

@ApiModel(value = "DatabaseBackupRecoverVm", description = "恢复")
class DatabaseBackupRecoverVm {

    @ApiModelProperty("备份id")
    @NotBlank(message = "备份id不可为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}