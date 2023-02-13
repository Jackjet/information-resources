package d1.project.d1project.system.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.d1project.system.model.vm.OrganizationUserInsertAllVm;
import d1.project.d1project.system.model.vm.WebAdminUserBaseVm;
import d1.project.d1project.system.service.OrganizationUserService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 管理端_系统管理_组织机构用户
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

    /**
     * 批量新增
     */
    @PostMapping(value = "/insertAll")
    public Result<String> insertAll(@Valid @RequestBody OrganizationUserInsertAllVm model, HttpServletRequest request) throws DoValidException {
        organizationUserService.insertAll(model, request);
        return ResultUtil.success("");
    }

    /**
     * 根据组织机构id获取角色选择
     */
    @GetMapping(value = "/findAllByOrganizationIdToUser")
    public Result<Page<WebAdminUserBaseVm>> findAllByOrganizationIdToUser(boolean selected, String organizationId, String roleName, String userName, PageableVm pageableVm) throws Exception {
        return ResultUtil.success("", organizationUserService.findAllByOrganizationId(selected, roleName, userName, organizationId, pageableVm));
    }


}
