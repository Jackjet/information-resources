package d1.project.dcrun.center.webapi.system.controller.webadmin;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.common.PageableVm;
import d1.project.dcrun.center.webapi.system.entity.Tenants;
import d1.project.dcrun.center.webapi.system.service.TenantsService;
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
 * @author libin
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/tenants")
@Api(value = "/webadmin/tenants", tags = "租户管理")
public class TenantsController {
    @Autowired
    private TenantsService tenantsService;

    @ApiOperation(value = "查询租户信息", notes = "查询租户信息")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<Tenants>> findAll(TenantsFindAllGetVm model) {
        try {
            Page<Tenants> result = this.tenantsService.findAllByName(model.getName(), model.getPage(), model.getSize());
            return ResultUtil.success("获取成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "新增租户信息", notes = "新增租户信息")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(HttpServletRequest request, @RequestBody @Valid TenantsInsertPostVm model) {
        try {
            this.tenantsService.insert(request, (JSONObject) JSONObject.toJSON(model));
            return ResultUtil.success("创建成功");
        } catch (Exception e) {
            return ResultUtil.fail("创建失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "编辑租户信息", notes = "编辑租户信息")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(HttpServletRequest request, @RequestBody @Valid TenantsUpdatePutVm model) {
        try {
            this.tenantsService.update(request, (JSONObject) JSONObject.toJSON(model));
            return ResultUtil.success("修改成功");
        } catch (Exception e) {
            return ResultUtil.fail("修改失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "重置密码", notes = "重置密码")
    @RequestMapping(value = "/resetPassword", method = RequestMethod.PUT)
    public Result<String> resetPassword(HttpServletRequest request, String id) {
        try {
            this.tenantsService.resetPassword(request, id);
            return ResultUtil.success("重置成功");
        } catch (Exception e) {
            return ResultUtil.fail("重置失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "获取数据详细信息", notes = "根据id来获取详细信息")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result<Tenants> findById(String id) {
        Tenants mUser = tenantsService.findById(id);
        return mUser != null ? ResultUtil.success("获取详细信息", mUser) : ResultUtil.fail("数据为空或获取失败!");
    }


    @ApiOperation(value = "删除数据", notes = "根据id来指定删除数据")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    public Result delete(String id) {
        try {
            tenantsService.delete(id);
        } catch (Exception e) {
            return ResultUtil.fail("删除失败:" + e.getMessage());
        }
        return ResultUtil.success("删除成功!");
    }
}

@ApiModel(value = "TenantsFindAllGetVm", description = "查询租户信息")
class TenantsFindAllGetVm extends PageableVm {
    @ApiModelProperty(value = "名称")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

@ApiModel(value = "TenantsInsertPostVm", description = "新增租户信息")
class TenantsInsertPostVm {
    @ApiModelProperty(value = "名称")
    @NotBlank(message = "租户名称不能为空")
    private String name;
    @ApiModelProperty(value = "集成项目数量")
    private Integer integrationProjectAmount;
    @ApiModelProperty(value = "备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIntegrationProjectAmount() {
        return integrationProjectAmount;
    }

    public void setIntegrationProjectAmount(Integer integrationProjectAmount) {
        this.integrationProjectAmount = integrationProjectAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "TenantsUpdatePutVm", description = "编辑租户信息")
class TenantsUpdatePutVm {
    @ApiModelProperty(value = "唯一标识")
    @NotBlank(message = "唯一标识不能为空")
    private String id;
    @ApiModelProperty(value = "集成项目数量")
    private Integer integrationProjectAmount;
    @ApiModelProperty(value = "备注")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getIntegrationProjectAmount() {
        return integrationProjectAmount;
    }

    public void setIntegrationProjectAmount(Integer integrationProjectAmount) {
        this.integrationProjectAmount = integrationProjectAmount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
