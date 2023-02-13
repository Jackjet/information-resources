package d1.project.tangshan.operation.manage.controller.webadmin.remote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.permission.entity.Role;
import d1.framework.permission.service.RoleService;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.tangshan.operation.manage.entity.remote.CommandType;
import d1.project.tangshan.operation.manage.service.remote.CommandTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author lin
 */
@Auth("webadmin")
@RestController("/webadmin/remoteControl/commandType")
@RequestMapping("/webadmin/remoteControl/commandType")
@Api(value = "/webadmin/remoteControl/commandType", tags = "运维指令分类管理")
public class CommandTypeController {
    private final CommandTypeService commandTypeService;
    private final RoleService roleService;

    public CommandTypeController(CommandTypeService commandTypeService, RoleService roleService) {
        this.commandTypeService = commandTypeService;
        this.roleService = roleService;
    }


    @ApiOperation(value = "新增")
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody CommandTypeInsertVm model, HttpServletRequest request) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            commandTypeService.insert(jsonObject, request);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除指令分类")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) {
        try {
            commandTypeService.delete(id, request);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "指令分类编辑")
    @PutMapping(value = "/update")
    public Result<String> update(HttpServletRequest request, @Valid @RequestBody CommandTypeUpdateVm model) {
        try {
            commandTypeService.update(request, (JSONObject) JSON.toJSON(model));
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "指令分类列表（分页）")
    @GetMapping(value = "/findAll")
    public Result<Page<CommandType>> findAll(CommandTypeFindAllVm model) {
        try {
            Page<CommandType> result = commandTypeService.findAll((JSONObject) JSON.toJSON(model));
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<CommandType> findById(String id) {
        try {
            return ResultUtil.success("成功", commandTypeService.findById(id));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查询所有角色")
    @GetMapping(value = "/findRoles")
    public Result<List<Role>> findRoles() {
        try {
            List<Role> result = roleService.findAllRoles();
            return ResultUtil.success("成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }


}

@ApiModel(value = "CommandTypeInsertVm", description = "新增")
class CommandTypeInsertVm {
    @ApiModelProperty("名称")
    @NotBlank(message = "类型名称不可为空")
    private String name;
    @ApiModelProperty("角色Id")
    private String roleIds;
    @ApiModelProperty("角色名称")
    private String roleNames;
    @ApiModelProperty("备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "CommandTypeUpdateVm", description = "编辑")
class CommandTypeUpdateVm {

    @ApiModelProperty("指令分类id")
    @NotBlank(message = "指令分类id不可为空")
    private String id;
    @ApiModelProperty("角色Id")
    private String roleIds;
    @ApiModelProperty("角色名称")
    private String roleNames;
    @ApiModelProperty("备注")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(String roleIds) {
        this.roleIds = roleIds;
    }

    public String getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(String roleNames) {
        this.roleNames = roleNames;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "CommandTypeFindAllVm", description = "查询")
class CommandTypeFindAllVm extends PageableVm {
    @ApiModelProperty(value = "名称")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
