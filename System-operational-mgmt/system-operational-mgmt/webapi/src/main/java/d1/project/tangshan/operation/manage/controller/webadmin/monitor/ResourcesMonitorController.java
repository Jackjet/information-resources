package d1.project.tangshan.operation.manage.controller.webadmin.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.tangshan.operation.manage.Constants;
import d1.project.tangshan.operation.manage.entity.ResourceMonitorEntity;
import d1.project.tangshan.operation.manage.model.PageableVm;
import d1.project.tangshan.operation.manage.model.TimePageableVm;
import d1.project.tangshan.operation.manage.service.BaseEnvironmentMonitorAlarmService;
import d1.project.tangshan.operation.manage.service.ResourceMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

/**
 * @Author chengh
 **/
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/resourcesMonitor")
@Api(value = "/webadmin/resourcesMonitor", tags = "资源监控")
public class ResourcesMonitorController {

    private final ResourceMonitorService resourceMonitorService;

    @Autowired
    public ResourcesMonitorController(ResourceMonitorService resourceMonitorService) {
        this.resourceMonitorService = resourceMonitorService;
    }

    @ApiOperation(value = "资源监控")
    @GetMapping(value = "/resourceMonitoring")
    public Result<JSONObject> resourceMonitoring(ResourceMonitorFindAllVm model) {
        try {
            return ResultUtil.success(Constants.SUCCESS, resourceMonitorService.resourceMonitoring((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @Auth("noauth")
    @ApiOperation(value = "添加资源监控")
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public Result<String> insert(@RequestBody ResourceMonitorInsertVm model) {
        try {
            this.resourceMonitorService.insert((JSONObject) JSON.toJSON(model));
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

}

class ResourceMonitorFindAllVm extends PageableVm {
    @ApiModelProperty(value = "开始时间")
    private String beginTime;
    @ApiModelProperty(value = "结束时间")
    private String endTime;
    @ApiModelProperty("节点")
    private String nodeName;

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

class ResourceMonitorInsertVm {
    @ApiModelProperty("内网IP地址")
    private String intranetIp;

    @ApiModelProperty("ram")
    private Long ram;

    @ApiModelProperty("rom")
    private Long rom;

    @ApiModelProperty("cpu")
    private Integer cpu;

    public String getIntranetIp() {
        return intranetIp;
    }

    public void setIntranetIp(String intranetIp) {
        this.intranetIp = intranetIp;
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
}