package d1.project.dcrun.center.webapi.system.controller.tenants;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.dcrun.center.webapi.common.PageableVm;
import d1.project.dcrun.center.webapi.system.entity.IntegrationProjectManage;
import d1.project.dcrun.center.webapi.system.service.IntegrationProjectManageService;
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
@Auth("tenants")
@RestController
@RequestMapping("/tenants/integrationProjectManage")
@Api(value = "/tenants/integrationProjectManage", tags = "用户管理")
public class IntegrationProjectManageController {
    @Autowired
    private IntegrationProjectManageService integrationProjectManageService;

    @ApiOperation(value = "查询用户管理", notes = "查询用户管理")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<IntegrationProjectManage>> findAll(HttpServletRequest request, IntegrationProjectManageFindAllGetVm model) {
        try {
            Page<IntegrationProjectManage> result = this.integrationProjectManageService.findAllByCode(request, model.getCode(), model.getPage(), model.getSize());
            return ResultUtil.success("获取成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "新增用户管理", notes = "新增用户管理")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(HttpServletRequest request, @RequestBody @Valid IntegrationProjectManageInsertPostVm model) {
        try {
            JSONObject jo = (JSONObject) JSONObject.toJSON(model);
            this.integrationProjectManageService.insert(request, jo);
            return ResultUtil.success("创建成功");
        } catch (Exception e) {
            return ResultUtil.fail("创建失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "编辑用户管理", notes = "编辑用户管理")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(HttpServletRequest request, @RequestBody @Valid IntegrationProjectManageUpdatePutVm model) {
        try {
            JSONObject jo = (JSONObject) JSONObject.toJSON(model);
            this.integrationProjectManageService.update(request, jo);
            return ResultUtil.success("修改成功");
        } catch (Exception e) {
            return ResultUtil.fail("修改失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "重置密码", notes = "重置密码")
    @RequestMapping(value = "/resetPassword", method = RequestMethod.PUT)
    public Result<String> resetPassword(HttpServletRequest request, String id) {
        try {
            this.integrationProjectManageService.resetPassword(request, id);
            return ResultUtil.success("重置成功");
        } catch (Exception e) {
            return ResultUtil.fail("重置失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "获取数据详细信息", notes = "根据id来获取详细信息")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result<IntegrationProjectManage> findById(String id) {
        IntegrationProjectManage mUser = integrationProjectManageService.findById(id);
        return mUser != null ? ResultUtil.success("获取详细信息", mUser) : ResultUtil.fail("数据为空或获取失败!");
    }


    @ApiOperation(value = "删除数据", notes = "根据id来指定删除数据")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    public Result delete(String id) {
        try {
            integrationProjectManageService.delete(id);
        } catch (Exception e) {
            return ResultUtil.fail("删除失败:" + e.getMessage());
        }
        return ResultUtil.success("删除成功!");
    }
}

@ApiModel(value = "IntegrationProjectManageFindAllGetVm", description = "查询用户管理")
class IntegrationProjectManageFindAllGetVm extends PageableVm {
    @ApiModelProperty(value = "管理员账号")
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

@ApiModel(value = "IntegrationProjectManageInsertPostVm", description = "新增用户管理")
class IntegrationProjectManageInsertPostVm {
    @ApiModelProperty(value = "管理员账号")
    @NotBlank(message = "管理员账号不能为空")
    private String code;
    @ApiModelProperty(value = "备注")
    private String remark;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "IntegrationProjectManageUpdatePutVm", description = "编辑用户管理")
class IntegrationProjectManageUpdatePutVm {
    @ApiModelProperty(value = "用户id")
    @NotBlank(message = "用户id不能为空")
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
