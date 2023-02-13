package d1.project.resourcesharingmgmt.system.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.system.entity.RoleEntity;
import d1.project.resourcesharingmgmt.system.model.vm.RoleFindAllVm;
import d1.project.resourcesharingmgmt.system.model.vm.RoleInsertVm;
import d1.project.resourcesharingmgmt.system.model.vm.RoleUpdateVm;
import d1.project.resourcesharingmgmt.system.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * d1project
 * 系统管理_角色权限
 *
 * @author kikki
 * @date 2020-09-09 09:35
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/system/role")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }


    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody RoleInsertVm model, HttpServletRequest request) throws DoValidException {
        roleService.insert(model, request);
        return ResultUtil.success("");
    }

    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) throws DoValidException {
        roleService.delete(id, request);
        return ResultUtil.success("");
    }

    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody RoleUpdateVm model, HttpServletRequest request) throws DoValidException {
        roleService.update(model, request);
        return ResultUtil.success("");
    }

    @GetMapping(value = "/findAll")
    public Result<Page<RoleEntity>> findAll(RoleFindAllVm model) throws Exception {
        return ResultUtil.success("", roleService.findAll(model));
    }

    @GetMapping(value = "/find")
    public Result<RoleEntity> find(String id) throws DoValidException {
        return ResultUtil.success("", roleService.find(id));
    }
}
