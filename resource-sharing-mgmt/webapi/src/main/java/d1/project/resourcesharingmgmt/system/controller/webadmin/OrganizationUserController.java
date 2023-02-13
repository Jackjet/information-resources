package d1.project.resourcesharingmgmt.system.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.resourcesharingmgmt.system.model.OrganizationTree;
import d1.project.resourcesharingmgmt.system.model.vm.OrganizationUserInsertAllVm;
import d1.project.resourcesharingmgmt.system.service.OrganizationUserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * d1project
 * 系统管理_组织机构
 *
 * @author kikki
 * @date 2020-09-09 09:56
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/system/organizationUser")
public class OrganizationUserController {

    private final
    OrganizationUserService organizationUserService;

    public OrganizationUserController(OrganizationUserService organizationUserService) {
        this.organizationUserService = organizationUserService;
    }


    @PostMapping(value = "/insertAll")
    public Result<String> insertAll(@Valid @RequestBody OrganizationUserInsertAllVm model, HttpServletRequest request) throws DoValidException {
        organizationUserService.insertAll(model, request);
        return ResultUtil.success("");
    }
    /**
     * 全部组织机构树
     */
    @GetMapping(value = "/findAllByOrganizationIdToUser")
    public Result<OrganizationTree> findAll(boolean selected, String organizationId, String roleName, String userName, PageableVm pageableVm) throws Exception {
        return ResultUtil.success("", organizationUserService.findAllByOrganizationId(selected, roleName, userName, organizationId,pageableVm));
    }



}
