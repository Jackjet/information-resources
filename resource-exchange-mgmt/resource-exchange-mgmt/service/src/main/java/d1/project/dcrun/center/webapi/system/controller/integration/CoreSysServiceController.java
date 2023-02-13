package d1.project.dcrun.center.webapi.system.controller.integration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.common.PageableVm;
import d1.project.dcrun.center.webapi.common.model.OperationLog;
import d1.project.dcrun.center.webapi.common.service.IOperationLogService;
import d1.project.dcrun.center.webapi.common.service.system.SysService;
import d1.project.dcrun.center.webapi.common.service.system.SysServiceService;
import d1.project.dcrun.center.webapi.system.model.CoreSysServiceServiceInfoVm;
import d1.project.dcrun.center.webapi.system.service.CoreSysServiceService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author xuaa
 */
@Auth("integration")
@RestController
@RequestMapping("/integration/sysService")
@Api(value = "/integration/sysService", tags = "系统服务")
public class CoreSysServiceController {

    @Autowired
    private CoreSysServiceService coreSysServiceService;

    @Autowired
    private SysServiceService sysServiceService;

    @Autowired
    private IOperationLogService iOperationLogService;

    @ApiOperation(value = "系统服务（列表）", notes = "查询系统服务")
    @RequestMapping(value = "/findAll", method = {RequestMethod.GET})
    public Result<Page<SysService>> findAll(@Valid SysServiceFindAllVm model) {
        try {
            return ResultUtil.success("成功", coreSysServiceService.findAll((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail("查询失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "系统服务命令服务版本列表", notes = "系统服务命令服务版本列表")
    @ApiImplicitParam(name = "integrationId", value = "用户ID", required = true, dataType = "String")
    @RequestMapping(value = "/selectVersion", method = {RequestMethod.GET})
    public Result selectVersion(String integrationId) {
        try {
            return ResultUtil.success("成功", this.sysServiceService.selectVersion(integrationId));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage(), e);
        }
    }

    /**
     * 用于查看EMQ和API系统服务详细信息
     */
    @ApiOperation(value = "系统服务（详情）", notes = "根据id来获取详细信息")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/findById", method = {RequestMethod.GET})
    public Result<JSONObject> findById(String id) {
        try {
            return ResultUtil.success("成功", coreSysServiceService.findByIdDetails(id));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "系统服务添加", notes = "系统服务添加")
    @RequestMapping(value = "/insert", method = {RequestMethod.POST})
    public Result<String> insert(@Valid @RequestBody SysServiceInsertVm model, HttpServletRequest request) {
        try {
            coreSysServiceService.insert((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail("添加失败" + e.getMessage(), e);
        }
    }

    /**
     * 用于编辑EMQ和API系统服务信息
     *
     * @param model
     * @param request
     * @return
     */
    @ApiOperation(value = "系统服务编辑", notes = "系统服务编辑")
    @RequestMapping(value = "/update", method = {RequestMethod.PUT})
    public Result<String> update(@Valid @RequestBody SysServiceUpdateVm model, HttpServletRequest request) {
        try {
            coreSysServiceService.update((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("保存成功");
        } catch (Exception e) {
            return ResultUtil.fail("保存失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "系统服务删除", notes = "系统服务删除")
    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    public Result<String> delete(String id) {
        try {
            coreSysServiceService.deleteById(id);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail("删除失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "系统服务当前/远程版本号及状态", notes = "系统服务的当前/远程版本号及状态")
    @RequestMapping(value = "/versionAndStatus", method = RequestMethod.GET)
    public Result<CoreSysServiceServiceInfoVm> versionAndStatus(@Valid SysServiceAppidVm model) {
        try {
            return ResultUtil.success("获取成功", coreSysServiceService.versionAndStatus((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查询系统服务是否安装过", notes = "查询系统服务是否安装过")
    @RequestMapping(value = "/isInstalled", method = RequestMethod.GET)
    public Result<Boolean> isInstalled(@Valid SysServiceAppidVm model) {
        try {
            return ResultUtil.success("获取成功", sysServiceService.isInstall(model.getAppid()));
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查询系统服务是否在线", notes = "查询系统服务是否在线")
    @RequestMapping(value = "/isOnline", method = RequestMethod.GET)
    public Result<Boolean> isOnline(@Valid SysServiceAppidVm model) {
        try {
            return ResultUtil.success("获取成功", coreSysServiceService.isOnline((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "webapi集成服务安装")
    @RequestMapping(value = "/install", method = {RequestMethod.POST})
    public Result<String> install(@Valid @RequestBody SysServiceAppidVm model) {
        try {
            coreSysServiceService.install((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("安装成功");
        } catch (Exception e) {
            return ResultUtil.fail("安装失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "代码备份(script目录下面文件压缩成zip) 并保存到备份管理中")
    @RequestMapping(value = "/codeBackup", method = {RequestMethod.POST})
    public Result<String> install(@Valid @RequestBody SysServiceCodeBackupVm model, HttpServletRequest request) throws Exception{
        try {
            coreSysServiceService.codeBackup((JSONObject) JSON.toJSON(model),request);
            iOperationLogService.insert(new OperationLog("系统服务-备份", "系统服务", "备份了代码", "备份了代码" + model.getName(),1), request);
            return ResultUtil.success("代码备份成功");
        } catch (Exception e) {
            iOperationLogService.insert(new OperationLog("系统服务-备份", "系统服务", "备份了代码", "备份了代码" + model.getName() ,0), request);
            return ResultUtil.fail("代码备份失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "远程更新配置文件(config目录下面文件)")
    @RequestMapping(value = "/upgradeConfig", method = {RequestMethod.PUT})
    public Result<String> upgradeConfig(@Valid @RequestBody SysServiceAppidVm model) {
        try {
            coreSysServiceService.upgradeConfig((JSONObject) JSONObject.toJSON(model));
            return ResultUtil.success("更新成功");
        } catch (Exception e) {
            return ResultUtil.fail("更新失败" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "远程更新脚本文件(script目录下面文件)")
    @RequestMapping(value = "/upgradeScript", method = {RequestMethod.PUT})
    public Result<String> upgradeScript(@Valid @RequestBody SysServiceAppidVm model) {
        try {
            coreSysServiceService.upgradeScript(model.getAppid());
            return ResultUtil.success("更新成功");
        } catch (Exception e) {
            return ResultUtil.fail("更新失败" + e.getMessage(), e);
        }
    }
}

@ApiModel(value = "SysServiceFindAllVm", description = "系统服务查询")
class SysServiceFindAllVm extends PageableVm {
    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id", required = true)
    private String integrationId;
    @ApiModelProperty(value = "系统服务所在的运行节点id", required = true)
    private String runNodeId;
    @ApiModelProperty(value = "系统服务名称")
    private String name;
    @ApiModelProperty(value = "系统服务模板类型")
    private String templateType;

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public String getRunNodeId() {
        return runNodeId;
    }

    public void setRunNodeId(String runNodeId) {
        this.runNodeId = runNodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }
}

@ApiModel(value = "SysServiceInsertVm", description = "系统服务添加")
class SysServiceInsertVm {
    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id")
    private String integrationId;

    @NotBlank(message = "系统服务所在的运行节点id不能为空")
    @ApiModelProperty(value = "系统服务所在的运行节点id")
    private String runNodeId;

    @NotBlank(message = "系统服务名称不能为空")
    @ApiModelProperty(value = "系统服务名称")
    private String name;

    @NotBlank(message = "系统服务模板Id不能为空")
    @ApiModelProperty(value = "系统服务模板Id")
    private String templateId;

    @NotBlank(message = "系统服务模板类型不能为空")
    @ApiModelProperty(value = "系统服务模板类型")
    private String templateType;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "系统服务版本号")
    private String version = "--,--,--";

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public String getRunNodeId() {
        return runNodeId;
    }

    public void setRunNodeId(String runNodeId) {
        this.runNodeId = runNodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateType() {
        return templateType;
    }

    public void setTemplateType(String templateType) {
        this.templateType = templateType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}

@ApiModel(value = "SysServiceUpdateVm", description = "系统服务编辑")
class SysServiceUpdateVm {
    @NotBlank(message = "系统服务id不能为空")
    @ApiModelProperty(value = "系统服务id")
    private String id;
    @NotBlank(message = "系统服务名称不能为空")
    @ApiModelProperty(value = "系统服务名称")
    private String name;
    @ApiModelProperty(value = "系统服务url")
    private String url;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "端口号")
    private String serverPort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }
}

@ApiModel(value = "SysServiceAppidVm", description = "appidVm")
class SysServiceAppidVm {
    @NotBlank(message = "系统服务appid不能为空")
    @ApiModelProperty(value = "系统服务appid")
    private String appid;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
}

@ApiModel(value = "SysServiceCodeBackupVm", description = "代码备份")
class SysServiceCodeBackupVm{
    @NotBlank(message = "备份名称不能为空")
    @ApiModelProperty(value = "备份名称")
    private String name;
    @NotBlank(message = "系统服务appid不能为空")
    @ApiModelProperty(value = "系统服务appid")
    private String appid;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}