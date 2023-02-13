package d1.project.tangshan.operation.manage.controller.webadmin.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.tangshan.operation.manage.model.BaseEnvironmentMonitorVm;
import d1.project.tangshan.operation.manage.model.PageableVm;
import d1.project.tangshan.operation.manage.service.BaseEnvironmentMonitorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @Author chengh
 **/
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/environment")
@Api(value = "/webadmin/environment", tags = "基础环境监控")
public class BaseEnvironmentMonitorController {

    private final BaseEnvironmentMonitorService baseEnvironmentMonitorService;

    @Autowired
    public BaseEnvironmentMonitorController(BaseEnvironmentMonitorService baseEnvironmentMonitorService) {
        this.baseEnvironmentMonitorService = baseEnvironmentMonitorService;
    }

    @Auth("noauth")
    @ApiOperation(value = "新增")
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody BaseEnvironmentMonitorEntityInsertVm model) {
        try {
            baseEnvironmentMonitorService.insert((JSONObject)JSONObject.toJSON(model));
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查询", notes = "查询所有数据")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<BaseEnvironmentMonitorVm>> findAll(HttpServletRequest request, EnvFindAllVm model) {
        try {
            Page<BaseEnvironmentMonitorVm> result = baseEnvironmentMonitorService.findAll((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("查询成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("查询失败：" + e.getMessage(), e);
        }
    }
}

class EnvFindAllVm extends PageableVm {
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

@ApiModel(value = "BaseEnvironmentMonitorEntityInsertVm", description = "新增")
class BaseEnvironmentMonitorEntityInsertVm {
    @ApiModelProperty("内网IP地址")
    private String intranetIp;

    @ApiModelProperty("数据库")
    private String db;

    @ApiModelProperty("系统软件")
    private String systemSoftware;

    public String getIntranetIp() {
        return intranetIp;
    }

    public void setIntranetIp(String intranetIp) {
        this.intranetIp = intranetIp;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getSystemSoftware() {
        return systemSoftware;
    }

    public void setSystemSoftware(String systemSoftware) {
        this.systemSoftware = systemSoftware;
    }
}