package d1.project.resourcesharingmgmt.system.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.system.entity.WebAdminUserEntity;
import d1.project.resourcesharingmgmt.system.model.OrganizationTree;
import d1.project.resourcesharingmgmt.system.model.vm.OrganizationInsertVm;
import d1.project.resourcesharingmgmt.system.model.vm.OrganizationUpdateSeqVm;
import d1.project.resourcesharingmgmt.system.model.vm.OrganizationUpdateVm;
import d1.project.resourcesharingmgmt.system.service.OrganizationService;
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
@RequestMapping("/webadmin/system/organization")
public class OrganizationController {

    private final OrganizationService organizationService;


    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody OrganizationInsertVm model, HttpServletRequest request) throws DoValidException {
        organizationService.insert(model, request);
        return ResultUtil.success("");
    }

    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam String id, HttpServletRequest request) throws DoValidException {
        organizationService.delete(id, request);
        return ResultUtil.success("");
    }

    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody OrganizationUpdateVm model, HttpServletRequest request) throws DoValidException {
        organizationService.update(model, request);
        return ResultUtil.success("");
    }

    /**
     * 序列更新
     */
    @PutMapping(value = "/updateSeq")
    public Result<String> updateSeq(@Valid @RequestBody OrganizationUpdateSeqVm model, HttpServletRequest request) throws DoValidException {
        organizationService.updateSeq(model, request);
        return ResultUtil.success("");
    }

    /**
     * 全部组织机构树
     */
    @GetMapping(value = "/findAll")
    public Result<OrganizationTree> findAll() {
        return ResultUtil.success("", organizationService.findAll());
    }

    /**
     * 根据id查询组织机构树
     */
    @GetMapping(value = "/findAllById")
    public Result<WebAdminUserEntity> find(String id, HttpServletRequest request) throws Exception{
        return ResultUtil.success("", organizationService.findAllById(id, request));
    }

}
