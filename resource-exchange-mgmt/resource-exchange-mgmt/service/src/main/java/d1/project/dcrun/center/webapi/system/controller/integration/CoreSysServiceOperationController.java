package d1.project.dcrun.center.webapi.system.controller.integration;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.common.PageableVm;
import d1.project.dcrun.center.webapi.common.service.operation.record.OperationRecord;
import d1.project.dcrun.center.webapi.system.service.CoreSysServiceOperationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author maoyy
 */

@Auth("integration")
@RestController
@RequestMapping("/integration/sysService/operation")
@Api(value = "/integration/sysService/operation", tags = "系统服务操作记录")
public class CoreSysServiceOperationController {

    @Autowired
    private CoreSysServiceOperationService coreSysServiceOperationService;

    @ApiOperation(value = "系统服务操作记录（列表）", notes = "查询系统服务操作记录")
    @RequestMapping(value = "/findAllByAppId", method = {RequestMethod.GET})
    public Result<Page<OperationRecord>> findAll(@Valid SysServiceOperationGetVm model) {
        try {
            return ResultUtil.success("成功", coreSysServiceOperationService.findAllByAppId((JSONObject) JSONObject.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail("查询失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查询所有的操作类型", notes = "查询系统服务操作记录")
    @RequestMapping(value = "/operationType", method = {RequestMethod.GET})
    public Result<JSONArray> findAllOperationType() {
        try {
            return ResultUtil.success("成功", coreSysServiceOperationService.findAllOperationType());
        } catch (Exception e) {
            return ResultUtil.fail("查询失败" + e.getMessage(), e);
        }
    }
}

@ApiModel(value = "SysServiceOperationGetVm", description = "系统服务操作记录vm")
class SysServiceOperationGetVm extends PageableVm {
    @NotBlank(message = "系统服务appid不能为空")
    @ApiModelProperty(value = "系统服务appid")
    private String appid;
    @ApiModelProperty(value = "系统服务名称")
    private String sysServiceName;
    @ApiModelProperty(value = "系统服务类型")
    private String sysServiceType;
    @ApiModelProperty(value = "操作类型")
    private String type;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSysServiceName() {
        return sysServiceName;
    }

    public void setSysServiceName(String sysServiceName) {
        this.sysServiceName = sysServiceName;
    }

    public String getSysServiceType() {
        return sysServiceType;
    }

    public void setSysServiceType(String sysServiceType) {
        this.sysServiceType = sysServiceType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}


