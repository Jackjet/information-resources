package d1.project.tangshan.operation.manage.controller.webadmin.operations.module;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.tangshan.operation.manage.entity.operations.module.Database;
import d1.project.tangshan.operation.manage.entity.operations.module.Node;
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
@RestController("/webadmin/operations/database")
@RequestMapping("/webadmin/operations/database")
@Api(value = "/webadmin/operations/database", tags = "数据库管理")
public class DatabaseController {
    private final DatabaseService databaseService;
    private final NodeService nodeService;

    public DatabaseController(DatabaseService databaseService, NodeService nodeService) {
        this.databaseService = databaseService;
        this.nodeService = nodeService;
    }

    @ApiOperation(value = "新增")
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody DatabaseInsertVm model, HttpServletRequest request) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            databaseService.insert(jsonObject, request);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除数据库")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) {
        try {
            databaseService.delete(id, request);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "数据库编辑")
    @PutMapping(value = "/update")
    public Result<String> update(HttpServletRequest request, @Valid @RequestBody DatabaseUpdateVm model) {
        try {
            databaseService.update(request, (JSONObject) JSON.toJSON(model));
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "数据库列表（分页）")
    @GetMapping(value = "/findAll")
    public Result<Page<Database>> findAll(DatabaseFindAllVm model) {
        try {
            Page<Database> result = databaseService.findAll((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<Database> findById(String id) {
        try {
            return ResultUtil.success("成功", databaseService.findById(id));
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
}

@ApiModel(value = "DatabaseInsertVm", description = "新增")
class DatabaseInsertVm {
    @ApiModelProperty("名称")
    @NotBlank(message = "数据库名称不可为空")
    private String name;
    @ApiModelProperty("节点Id")
    @NotBlank(message = "节点Id不可为空")
    private String nodeId;
    @ApiModelProperty("节点名称")
    @NotBlank(message = "节点名称不可为空")
    private String nodeName;
    @ApiModelProperty("进程名")
    private String processName;
    @ApiModelProperty("占用端口")
    private String port;
    @ApiModelProperty("所属系统和平台")
    private String systemAndPlatform;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("数据库类型（sqlserver,mysql,postgre,oracle）")
    @NotBlank(message = "数据库类型不可为空")
    private String type;

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

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSystemAndPlatform() {
        return systemAndPlatform;
    }

    public void setSystemAndPlatform(String systemAndPlatform) {
        this.systemAndPlatform = systemAndPlatform;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

@ApiModel(value = "DatabaseUpdateVm", description = "编辑")
class DatabaseUpdateVm {

    @ApiModelProperty("数据库id")
    @NotBlank(message = "数据库id不可为空")
    private String id;
    @ApiModelProperty("进程名")
    private String processName;
    @ApiModelProperty("占用端口")
    private String port;
    @ApiModelProperty("所属系统和平台")
    private String systemAndPlatform;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("备注")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSystemAndPlatform() {
        return systemAndPlatform;
    }

    public void setSystemAndPlatform(String systemAndPlatform) {
        this.systemAndPlatform = systemAndPlatform;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "DatabaseFindAllVm", description = "查询")
class DatabaseFindAllVm extends PageableVm {
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty("节点Id")
    private String nodeId;
    @ApiModelProperty("所属系统和平台")
    private String systemAndPlatform;

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

    public String getSystemAndPlatform() {
        return systemAndPlatform;
    }

    public void setSystemAndPlatform(String systemAndPlatform) {
        this.systemAndPlatform = systemAndPlatform;
    }
}
