package d1.project.tangshan.operation.manage.controller.webadmin.database;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.tangshan.operation.manage.entity.operations.module.Database;
import d1.project.tangshan.operation.manage.entity.operations.module.Node;
import d1.project.tangshan.operation.manage.entity.database.DatabaseMonitoring;
import d1.project.tangshan.operation.manage.service.operations.module.DatabaseService;
import d1.project.tangshan.operation.manage.service.operations.module.NodeService;
import d1.project.tangshan.operation.manage.service.database.DatabaseMonitoringService;
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
@RestController("/webadmin/databaseOperation/databaseMonitoring")
@RequestMapping("/webadmin/databaseOperation/databaseMonitoring")
@Api(value = "/webadmin/databaseOperation/databaseMonitoring", tags = "数据库监控管理")
public class DatabaseMonitoringController {
    private final DatabaseMonitoringService databaseMonitoringService;
    private final NodeService nodeService;
    private final DatabaseService databaseService;

    public DatabaseMonitoringController(DatabaseMonitoringService databaseMonitoringService, NodeService nodeService, DatabaseService databaseService) {
        this.databaseMonitoringService = databaseMonitoringService;
        this.nodeService = nodeService;
        this.databaseService = databaseService;
    }
    @ApiOperation(value = "新增")
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody DatabaseMonitoringInsertVm model, HttpServletRequest request) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            databaseMonitoringService.insert(jsonObject, request);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除数据库监控")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) {
        try {
            databaseMonitoringService.delete(id, request);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "数据库监控编辑")
    @PutMapping(value = "/update")
    public Result<String> update(HttpServletRequest request, @Valid @RequestBody DatabaseMonitoringUpdateVm model) {
        try {
            databaseMonitoringService.update(request, (JSONObject) JSON.toJSON(model));
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "数据库监控列表（分页）")
    @GetMapping(value = "/findAll")
    public Result<Page<DatabaseMonitoring>> findAll(DatabaseMonitoringFindAllVm model) {
        try {
            Page<DatabaseMonitoring> result = databaseMonitoringService.findAll((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<DatabaseMonitoring> findById(String id) {
        try {
            return ResultUtil.success("成功", databaseMonitoringService.findById(id));
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
@ApiModel(value = "DatabaseMonitoringInsertVm", description = "新增")
class DatabaseMonitoringInsertVm {
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
    @ApiModelProperty("内存阈值")
    private Long ram;
    @ApiModelProperty("存储阈值")
    private Long rom;
    @ApiModelProperty("告警方式0 短信；1 邮件")
    @NotBlank(message = "告警方式不可为空")
    private String alarmWay;
    @ApiModelProperty("通知人(以英文 , 隔开)")
    private String peopleNotified;

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

    public Long getRam() {
        return ram;
    }

    public void setRam(Long ram) {
        this.ram = ram;
    }

    public Long getRom() {
        return rom;
    }

    public void setRom(Long rom) {
        this.rom = rom;
    }

    public String getAlarmWay() {
        return alarmWay;
    }

    public void setAlarmWay(String alarmWay) {
        this.alarmWay = alarmWay;
    }

    public String getPeopleNotified() {
        return peopleNotified;
    }

    public void setPeopleNotified(String peopleNotified) {
        this.peopleNotified = peopleNotified;
    }
}

@ApiModel(value = "DatabaseMonitoringUpdateVm", description = "编辑")
class DatabaseMonitoringUpdateVm {

    @ApiModelProperty("数据库监控id")
    @NotBlank(message = "数据库监控id不可为空")
    private String id;
    @ApiModelProperty("内存阈值")
    private Long ram;
    @ApiModelProperty("存储阈值")
    private Long rom;
    @ApiModelProperty("告警方式0 短信；1 邮件")
    @NotBlank(message = "告警方式不可为空")
    private String alarmWay;
    @ApiModelProperty("通知人(以英文 , 隔开)")
    private String peopleNotified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getRam() {
        return ram;
    }

    public void setRam(Long ram) {
        this.ram = ram;
    }

    public Long getRom() {
        return rom;
    }

    public void setRom(Long rom) {
        this.rom = rom;
    }

    public String getAlarmWay() {
        return alarmWay;
    }

    public void setAlarmWay(String alarmWay) {
        this.alarmWay = alarmWay;
    }

    public String getPeopleNotified() {
        return peopleNotified;
    }

    public void setPeopleNotified(String peopleNotified) {
        this.peopleNotified = peopleNotified;
    }
}

@ApiModel(value = "DatabaseMonitoringFindAllVm", description = "查询")
class DatabaseMonitoringFindAllVm extends PageableVm {
    @ApiModelProperty(value = "名称")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}