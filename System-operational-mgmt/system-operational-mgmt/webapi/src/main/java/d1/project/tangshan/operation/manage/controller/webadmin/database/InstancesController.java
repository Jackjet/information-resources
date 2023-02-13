package d1.project.tangshan.operation.manage.controller.webadmin.database;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.tangshan.operation.manage.entity.operations.module.Database;
import d1.project.tangshan.operation.manage.entity.operations.module.Node;
import d1.project.tangshan.operation.manage.entity.database.Instances;
import d1.project.tangshan.operation.manage.service.operations.module.DatabaseService;
import d1.project.tangshan.operation.manage.service.operations.module.NodeService;
import d1.project.tangshan.operation.manage.service.database.InstancesService;
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
@RestController("/webadmin/databaseOperation/instances")
@RequestMapping("/webadmin/databaseOperation/instances")
@Api(value = "/webadmin/databaseOperation/instances", tags = "实例管理")
public class InstancesController {
    private final InstancesService instancesService;
    private final NodeService nodeService;
    private final DatabaseService databaseService;

    public InstancesController(InstancesService instancesService, NodeService nodeService, DatabaseService databaseService) {
        this.instancesService = instancesService;
        this.nodeService = nodeService;
        this.databaseService = databaseService;
    }


    @ApiOperation(value = "新增")
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody InstancesInsertVm model, HttpServletRequest request) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            instancesService.insert(jsonObject, request);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除实例")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) {
        try {
            instancesService.delete(id, request);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "实例编辑")
    @PutMapping(value = "/update")
    public Result<String> update(HttpServletRequest request, @Valid @RequestBody InstancesUpdateVm model) {
        try {
            instancesService.update(request, (JSONObject) JSON.toJSON(model));
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "实例列表（分页）")
    @GetMapping(value = "/findAll")
    public Result<Page<Instances>> findAll(InstancesFindAllVm model) {
        try {
            Page<Instances> result = instancesService.findAll((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<Instances> findById(String id) {
        try {
            return ResultUtil.success("成功", instancesService.findById(id));
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
}
@ApiModel(value = "InstancesInsertVm", description = "新增")
class InstancesInsertVm {
    @ApiModelProperty("名称")
    @NotBlank(message = "实例名称不可为空")
    private String name;
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
    @ApiModelProperty("配置id")
    private String configId;
    @ApiModelProperty("配置名称")
    private String configName;
    @ApiModelProperty("备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "InstancesUpdateVm", description = "编辑")
class InstancesUpdateVm {

    @ApiModelProperty("实例id")
    @NotBlank(message = "实例id不可为空")
    private String id;
    @ApiModelProperty("配置id")
    private String configId;
    @ApiModelProperty("配置名称")
    private String configName;
    @ApiModelProperty("备注")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "InstancesFindAllVm", description = "查询")
class InstancesFindAllVm extends PageableVm {
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty("节点id")
    private String nodeId;
    @ApiModelProperty("数据库id")
    private String databaseId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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