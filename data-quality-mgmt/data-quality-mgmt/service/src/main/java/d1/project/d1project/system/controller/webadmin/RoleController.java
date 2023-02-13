package d1.project.d1project.system.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.system.entity.RoleEntity;
import d1.project.d1project.system.model.vm.RoleFindAllVm;
import d1.project.d1project.system.model.vm.RoleInsertVm;
import d1.project.d1project.system.model.vm.RoleUpdateVm;
import d1.project.d1project.system.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 管理端_系统管理_角色权限
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


    /**
     * 新增
     */
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody RoleInsertVm model, HttpServletRequest request) throws DoValidException {
        roleService.insert(model, request);
        return ResultUtil.success("");
    }

    /**
     * 删除
     */
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) throws DoValidException {
        roleService.delete(id, request);
        return ResultUtil.success("");
    }

    /**
     * 更新
     */
    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody RoleUpdateVm model, HttpServletRequest request) throws DoValidException {
        roleService.update(model, request);
        return ResultUtil.success("");
    }

    /**
     * 分页查询
     */
    @GetMapping(value = "/findAll")
    public Result<Page<RoleEntity>> findAll(RoleFindAllVm model) throws Exception {
        return ResultUtil.success("", roleService.findAll(model));
    }

    /**
     * 查看详情
     */
    @GetMapping(value = "/find")
    public Result<RoleEntity> find(String id) throws DoValidException {
        return ResultUtil.success("", roleService.find(id));
    }
}
