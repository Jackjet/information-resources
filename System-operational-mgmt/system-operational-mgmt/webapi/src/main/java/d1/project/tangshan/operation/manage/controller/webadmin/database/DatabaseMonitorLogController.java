package d1.project.tangshan.operation.manage.controller.webadmin.database;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.tangshan.operation.manage.service.database.DatabaseMonitorLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author libin
 **/
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/databaseMonitorLog")
@Api(value = "/webadmin/databaseMonitorLog", tags = "数据库监控")
public class DatabaseMonitorLogController {
    private final DatabaseMonitorLogService databaseMonitorLogService;

    public DatabaseMonitorLogController(DatabaseMonitorLogService databaseMonitorLogService) {
        this.databaseMonitorLogService = databaseMonitorLogService;
    }

    @Auth("noauth")
    @ApiOperation(value = "新增")
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody DatabaseMonitorLogInsertVm model) {
        try {
            databaseMonitorLogService.insert((JSONObject) JSONObject.toJSON(model));
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
}

@ApiModel(value = "DatabaseMonitorLogInsertVm", description = "新增")
class DatabaseMonitorLogInsertVm {
    @ApiModelProperty("节点")
    private String nodeName;

    @ApiModelProperty("节点")
    private String nodeId;

    @ApiModelProperty("数据库")
    private String database;

    @ApiModelProperty("数据库id")
    private String databaseId;

    @ApiModelProperty("数据资源")
    private String dataSource;

    @ApiModelProperty("内存资源")
    private String memorySource;

    @ApiModelProperty("运行状态")
    private String status;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getMemorySource() {
        return memorySource;
    }

    public void setMemorySource(String memorySource) {
        this.memorySource = memorySource;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
