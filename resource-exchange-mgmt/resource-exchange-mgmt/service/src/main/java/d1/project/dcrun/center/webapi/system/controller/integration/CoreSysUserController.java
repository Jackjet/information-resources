package d1.project.dcrun.center.webapi.system.controller.integration;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.common.PageableVm;
import d1.project.dcrun.center.webapi.common.service.sys.user.SysUser;
import d1.project.dcrun.center.webapi.system.service.CoreSysUserService;
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
 * @author maoyy
 */
@Auth("integration")
@RestController
@RequestMapping("/integration/sysUser")
@Api(value = "/integration/sysUser", tags = "系统服务开发者信息")
public class CoreSysUserController {

    @Autowired
    private CoreSysUserService coreSysUserService;

    @ApiOperation(value = "查询开发者信息", notes = "查询开发者信息")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<SysUser>> findAll(@Valid SysUserFindAllVm model) {
        try {
            return ResultUtil.success("获取成功", coreSysUserService.findAll((JSONObject) JSONObject.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "系统开发者（详情）", notes = "根据id来获取详细信息")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/findById", method = {RequestMethod.GET})
    public Result<SysUser> findById(String id) {
        try {
            return ResultUtil.success("成功", coreSysUserService.findByIdDetails(id));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "开发者信息添加", notes = "开发者信息添加")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(HttpServletRequest request, @Valid @RequestBody SysUserInsertVm model) {
        try {
            coreSysUserService.insert(request, (JSONObject) JSONObject.toJSON(model));
            return ResultUtil.success("创建成功");
        } catch (Exception e) {
            return ResultUtil.fail("创建失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "修改开发者信息", notes = "修改开发者信息")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(HttpServletRequest request, @Valid @RequestBody SysUserUpdateVm model) {
        try {
            coreSysUserService.update(request, (JSONObject) JSONObject.toJSON(model));
            return ResultUtil.success("修改成功");
        } catch (Exception e) {
            return ResultUtil.fail("修改失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除开发者信息", notes = "删除开发者信息")
    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    public Result<String> delete(String id) {
        try {
            coreSysUserService.deleteById(id);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail("删除失败：" + e.getMessage(), e);
        }
    }

}

@ApiModel(value = "SysUserFindAllVm", description = "查询开发者信息")
class SysUserFindAllVm extends PageableVm {
    @NotBlank(message = "集成项目id不能为空")
    @ApiModelProperty(value = "集成项目id")
    private String integrationId;
    @ApiModelProperty(value = "开发者名称")
    private String name;

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

@ApiModel(value = "SysUserInsertVm", description = "添加开发者信息")
class SysUserInsertVm {
    @NotBlank(message = "集成项目id不能为空")
    @ApiModelProperty(value = "集成项目id")
    private String integrationId;
    @NotBlank(message = "请选择开发者创建类型")
    @ApiModelProperty(value = "是否自动创建")
    private String isAutomatic;
    @ApiModelProperty(value = "开发者id")
    private String appid;
    @ApiModelProperty(value = "开发者密钥")
    private String appkey;
    @NotBlank(message = "开发者名称不能为空")
    @ApiModelProperty(value = "开发者名称")
    private String name;
    @ApiModelProperty(value = "备注")
    private String remark;

    public String getIntegrationId() {
        return integrationId;
    }

    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public String getIsAutomatic() {
        return isAutomatic;
    }

    public void setIsAutomatic(String isAutomatic) {
        this.isAutomatic = isAutomatic;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "SysUserUpdateVm", description = "添加开发者信息")
class SysUserUpdateVm {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "备注")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

