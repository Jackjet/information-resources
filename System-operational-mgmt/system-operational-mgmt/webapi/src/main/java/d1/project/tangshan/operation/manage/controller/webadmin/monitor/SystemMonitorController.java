package d1.project.tangshan.operation.manage.controller.webadmin.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.tangshan.operation.manage.entity.SystemMonitorEntity;
import d1.project.tangshan.operation.manage.model.PageableVm;
import d1.project.tangshan.operation.manage.service.SystemMonitorService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;

/**
 * @Author chengh
 **/
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/systemMonitor")
@Api(value = "/webadmin/systemMonitor", tags = "系统监控")
public class SystemMonitorController {

    private final SystemMonitorService systemMonitorService;

    @Autowired
    public SystemMonitorController(SystemMonitorService systemMonitorService) {
        this.systemMonitorService = systemMonitorService;
    }

    @Auth("noauth")
    @ApiOperation(value = "新增")
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody SystemMonitorInsertVm model) {
        try {
            SystemMonitorEntity systemMonitor = MyUtils.model2Entity(model, SystemMonitorEntity.class);
            systemMonitor.setId(MyUtils.generate32Id());
            systemMonitor.setCreateTime(Calendar.getInstance());
            systemMonitor.setLogTime(Calendar.getInstance());
            systemMonitorService.insert(systemMonitor);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查询", notes = "查询所有数据")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<SystemMonitorEntity>> findAll(HttpServletRequest request, SystemFindAllVm model) {
        try {
            Page<SystemMonitorEntity> result = systemMonitorService.findAll((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("查询成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("查询失败：" + e.getMessage(), e);
        }
    }
}

class SystemFindAllVm extends PageableVm {
    @ApiModelProperty("节点名称")
    private String nodeName;
    @ApiModelProperty("开始时间")
    private String beginTime;
    @ApiModelProperty("结束时间")
    private String endTime;

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}

@ApiModel(value = "SystemMonitorInsertVm", description = "新增")
class SystemMonitorInsertVm {
    @ApiModelProperty("节点")
    private String nodeName;

    @ApiModelProperty("节点")
    private String nodeId;

    @ApiModelProperty("系统")
    private String system;

    @ApiModelProperty("系统id")
    private String systemId;

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

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
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