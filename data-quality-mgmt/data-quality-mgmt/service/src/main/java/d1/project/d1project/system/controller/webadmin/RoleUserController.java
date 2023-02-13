package d1.project.d1project.system.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.d1project.system.entity.RoleUserEntity;
import d1.project.d1project.system.model.vm.RoleUserFindRoleIdVm;
import d1.project.d1project.system.model.vm.RoleUserInsertAllVm;
import d1.project.d1project.system.model.vm.RoleUserMenuTreeVm;
import d1.project.d1project.system.model.vm.WebAdminUserBaseVm;
import d1.project.d1project.system.service.RoleUserService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 管理端_系统管理_角色权限用户
 *
 * @author kikki
 * @date 2020-09-09 09:35
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/system/roleUser")
public class RoleUserController {

    private final RoleUserService roleUserService;

    public RoleUserController(RoleUserService roleUserService) {
        this.roleUserService = roleUserService;
    }

    /**
     * 角色批量新增用户
     */
    @PostMapping(value = "/insertAll")
    public Result<String> insertAll(@Valid @RequestBody RoleUserInsertAllVm model, HttpServletRequest request) throws DoValidException {
        roleUserService.insertAll(model, request);
        return ResultUtil.success("");
    }

    /**
     * 根据角色查找用户
     */
    @GetMapping(value = "/findRoleId")
    public Result<Page<RoleUserEntity>> findRoleId(@Valid RoleUserFindRoleIdVm model) throws Exception {
        return ResultUtil.success("", roleUserService.findRoleId(model));
    }

    /**
     * 根据查询条件查询已选未选
     */
    @GetMapping(value = "/findAllByRoleIdToUser")
    public Result<Page<WebAdminUserBaseVm>> findAllByRoleId(boolean selected, String roleId, String userName, String organizationName, PageableVm pageableVm) throws Exception {
        return ResultUtil.success("", roleUserService.findAllByRoleId(selected, roleId, userName, organizationName, pageableVm));
    }

    /**
     * 根据当前用户获取角色权限
     */
    @GetMapping(value = "/findCurrentUser")
    public Result<List<RoleUserMenuTreeVm>> findCurrentUser(HttpServletRequest request) throws DoValidException {
        return ResultUtil.success("", roleUserService.findCurrentUser(request));
    }

    /**
     * 获取用户角色菜单按钮
     */
    @GetMapping(value = "/findCurrentUserButton")
    public Result<List<String>> findCurrentUserButton(HttpServletRequest request) throws DoValidException {
        return ResultUtil.success("", roleUserService.findCurrentUserButton(request));
    }

    /**
     * 根据当前用户获取角色侧边栏
     */
    @GetMapping(value = "/findCurrentUserSidebar")
    public Result<List<RoleUserMenuTreeVm>> findCurrentUserSidebar(HttpServletRequest request) throws DoValidException {
        return ResultUtil.success("", roleUserService.findCurrentUserSidebar(request));
    }
}
