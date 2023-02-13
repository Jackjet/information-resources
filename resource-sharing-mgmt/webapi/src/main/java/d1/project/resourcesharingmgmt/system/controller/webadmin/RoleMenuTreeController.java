package d1.project.resourcesharingmgmt.system.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.system.model.vm.RoleMenuTreeInsertVm;
import d1.project.resourcesharingmgmt.system.service.RoleMenuTreeService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * d1Project
 * 系统管理_角色权限
 *
 * @author kikki
 * @date 2020-09-10 19:09
 */

@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/system/roleMenuTree")
public class RoleMenuTreeController {

    private final RoleMenuTreeService roleMenuTreeService;

    public RoleMenuTreeController(RoleMenuTreeService roleMenuTreeService) {
        this.roleMenuTreeService = roleMenuTreeService;
    }

    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody RoleMenuTreeInsertVm model, HttpServletRequest request) throws DoValidException {
        roleMenuTreeService.insert(model, request);
        return ResultUtil.success("");
    }

    @GetMapping(value = "/findAllRoleIdChoose")
    public Result<List<String>> findAll(@Valid @NotBlank(message = "角色id不可为空") String roleId) {
        return ResultUtil.success("成功", roleMenuTreeService.findAllRoleIdChoose(roleId));

    }
}
