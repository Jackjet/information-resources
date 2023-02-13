package d1.project.resourcesharingmgmt.resource.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.business.ResourceUseInfoBusiness;
import d1.project.resourcesharingmgmt.resource.entity.ResourceUseInfoEntity;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo.*;
import d1.project.resourcesharingmgmt.resource.service.ResourceUseInfoService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 管理端-资源使用管理
 *
 * @author zhengyang
 */
@Auth("webadmin")
@RestController("/webadmin/resourceUseInfo")
@RequestMapping("/webadmin/resourceUseInfo")
public class ResourceUseInfoController {

    private final ResourceUseInfoBusiness resourceUseInfoBusiness;
    private final ResourceUseInfoService resourceUseInfoService;

    public ResourceUseInfoController(ResourceUseInfoBusiness resourceUseInfoBusiness,ResourceUseInfoService resourceUseInfoService) {
        this.resourceUseInfoBusiness = resourceUseInfoBusiness;
        this.resourceUseInfoService=resourceUseInfoService;
    }

    /**
     * 初审
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @PutMapping(value = "/audit")
    public Result<String> audit(@Valid @RequestBody ResourceUseInfoAuditVm model, HttpServletRequest request) throws Exception {
        resourceUseInfoBusiness.audit(model, request);
        return ResultUtil.success("");
    }

    /**
     * 审批
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody ResourceUseInfoUpdateVm model, HttpServletRequest request) throws Exception {
        resourceUseInfoBusiness.update(model, request);
        return ResultUtil.success("");
    }

    /**
     * 详情
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findById")
    public Result<ResourceUseInfoEntity> find(String id) throws DoValidException {
        return ResultUtil.success("", resourceUseInfoBusiness.find(id));
    }

    /**
     * 查询分页列表
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findAll")
    public Result<ResourceUseInfoEntity> findAll(ResourceUseInfoFindAllVm model,HttpServletRequest request) throws Exception{
        return ResultUtil.success("", resourceUseInfoBusiness.findAllByProvOrgId(model,request));
    }

    /**
     * 资源申请方统计详情
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findAllByProvOrgId")
    public Result<ResourceUseInfoEntity> findAllByProvOrgId(ResourceUseInfoFindAllByProvOrgIdVm model, HttpServletRequest request) throws Exception{
        return ResultUtil.success("", resourceUseInfoBusiness.findAllByProvOrgId(model,request));
    }

    /**
     * 资源申请方统计详情导出
     * @param model
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/exportByProvOrgId")
    public Result<String> exportByProvOrgId(ResourceUseInfoFindAllByProvOrgIdVm model, HttpServletResponse response) throws Exception{
        List<ResourceUseExcelExport> allList = resourceUseInfoBusiness.findExportByProvOrgId(model);
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        resourceUseInfoBusiness.easyExcelWrite(response, allList);
        return null;
    }
}
