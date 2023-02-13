package d1.project.resourcesharingmgmt.system.controller.api;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.system.entity.OrganizationEntity;
import d1.project.resourcesharingmgmt.system.model.OrganizationTree;
import d1.project.resourcesharingmgmt.system.service.IWebAdminUserService;
import d1.project.resourcesharingmgmt.system.service.OrganizationService;
import d1.project.resourcesharingmgmt.system.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * d1project
 * 系统管理_组织机构
 *
 * @author kikki
 * @date 2020-09-09 09:56
 */
@Auth("noauth")
@RestController
@RequestMapping("/api")
public class ApiController {
    private final OrganizationService organizationService;
    private final IWebAdminUserService webAdminUserService;
    private final RoleService roleService;

    public ApiController(OrganizationService organizationService,IWebAdminUserService webAdminUserService, RoleService roleService) {
        this.organizationService = organizationService;
        this.webAdminUserService = webAdminUserService;
        this.roleService = roleService;
    }

    /**
     * 全部组织机构树
     */
    @GetMapping(value = "/organization/findAll")
    public Result<OrganizationTree> findAllOrganization() {
        return ResultUtil.success("", organizationService.findAll());
    }

    /**
     * 全部组织机构
     */
    @GetMapping(value = "/organization/findAllList")
    public Result<List<OrganizationEntity>> findAllOrganizationList() {
        return ResultUtil.success("", organizationService.findAllList());
    }

    /**
     * 全部角色
     */
    @GetMapping(value = "/role/findAllList")
    public Result<List<OrganizationEntity>> findAllRoleList() {
        return ResultUtil.success("", roleService.findAllList());
    }

    /**
     * 全部用户
     */
    @GetMapping(value = "/user/findAllList")
    public Result<List<OrganizationEntity>> findAllUserList() throws Exception{
        return ResultUtil.success("", webAdminUserService.findAllList());
    }
}
