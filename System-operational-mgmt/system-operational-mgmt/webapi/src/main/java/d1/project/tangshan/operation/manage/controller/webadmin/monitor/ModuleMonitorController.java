package d1.project.tangshan.operation.manage.controller.webadmin.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.tangshan.operation.manage.entity.ModuleMonitorEntity;
import d1.project.tangshan.operation.manage.model.PageableVm;
import d1.project.tangshan.operation.manage.service.ModuleMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @Author chengh
 **/
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/moduleMonitor")
@Api(value = "/webadmin/moduleMonitor", tags = "模块监控")
public class ModuleMonitorController {

    private final ModuleMonitorService moduleMonitorService;

    @Autowired
    public ModuleMonitorController(ModuleMonitorService moduleMonitorService) {
        this.moduleMonitorService = moduleMonitorService;
    }

    @ApiOperation(value = "查询", notes = "查询所有数据")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<ModuleMonitorEntity>> findAll(HttpServletRequest request, @Valid ModuleMonitorFindAllVm model) {
        try {
            Page<ModuleMonitorEntity> result = moduleMonitorService.findAll((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("查询成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("查询失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<ModuleMonitorEntity> findById(String id) {
        try {
            return ResultUtil.success("成功", moduleMonitorService.findById(id));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "插入", notes = "插入数据")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(HttpServletRequest request, @Valid @RequestBody ModuleMonitorInsertVm model) {
        try {
            this.moduleMonitorService.insert((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail( e.getMessage(), e);
        }
    }

    @ApiOperation(value = "更新", notes = "更新数据")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(HttpServletRequest request, @Valid @RequestBody ModuleMonitorUpdateVm model) {
        try {
            this.moduleMonitorService.update((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail( e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除", notes = "删除数据")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<String> delete(@Valid @RequestBody ModuleMonitorDeleteVm model,HttpServletRequest request) {
        try {
            this.moduleMonitorService.delete(model.getId(),request);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail( e.getMessage(), e);
        }
    }
}

class ModuleMonitorFindAllVm extends PageableVm {
    @NotBlank(message = "监控类型不能为空")
    @ApiModelProperty("监控类型，性能监控(performance)、数据监控(data)、资源监控(resources)")
    private String type;
    @ApiModelProperty("节点名称")
    private String nodeName;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
}

class ModuleMonitorInsertVm {
    @NotBlank(message = "监控类型不能为空")
    @ApiModelProperty("监控类型，性能监控(performance)、数据监控(data)、资源监控(resources)")
    private String type;
    @ApiModelProperty("节点id")
    private String nodeId;
    @ApiModelProperty("节点名称")
    private String nodeName;
    @ApiModelProperty("系统id")
    private String sysId;
    @ApiModelProperty("系统名称")
    private String sysName;
    @ApiModelProperty("用户访问次数阈值/日")
    private Integer access;
    @ApiModelProperty("内存阈值(M)")
    private Long ram;
    @ApiModelProperty("存储阈值(M)")
    private Long rom;
    @ApiModelProperty("cpu使用阈值(%)")
    private Integer cpu;
    @NotBlank(message = "告警方式不能为空")
    @ApiModelProperty("告警方式，短信(sms)、邮件(email)、锁定账号(account)")
    private String alarmWay;
    @ApiModelProperty("通知人")
    private String peopleNotified;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getSysName() {
        return sysName;
    }

    public void setSysName(String sysName) {
        this.sysName = sysName;
    }

    public Integer getAccess() {
        return access;
    }

    public void setAccess(Integer access) {
        this.access = access;
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

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
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

class ModuleMonitorUpdateVm {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("用户访问次数阈值/日")
    private Integer access;
    @ApiModelProperty("内存阈值(M)")
    private Long ram;
    @ApiModelProperty("存储阈值(M)")
    private Long rom;
    @ApiModelProperty("cpu使用阈值(%)")
    private Integer cpu;
    @NotBlank(message = "告警方式不能为空")
    @ApiModelProperty("告警方式，短信(sms)、邮件(email)、锁定账号(account)")
    private String alarmWay;
    @ApiModelProperty("通知人")
    private String peopleNotified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAccess() {
        return access;
    }

    public void setAccess(Integer access) {
        this.access = access;
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

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
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

class ModuleMonitorDeleteVm {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
