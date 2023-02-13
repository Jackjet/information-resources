package d1.project.resourcesharingmgmt.resource.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.business.DemandedInfoBusiness;
import d1.project.resourcesharingmgmt.resource.entity.DemandedInfoEntity;
import d1.project.resourcesharingmgmt.resource.model.DemandedInfo.DemandedInfoFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.DemandedInfo.DemandedInfoInsertVm;
import d1.project.resourcesharingmgmt.resource.model.DemandedInfo.DemandedInfoVm;
import d1.project.resourcesharingmgmt.resource.model.DemandedInfoAnalysisVm;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 门户端_需求管理
 *
 * @author zhengyang
 */
@Auth("webadmin")
@RestController("/web/demandedInfo")
@RequestMapping("/web/demandedInfo")
public class DemandedInfoController {

    private final DemandedInfoBusiness demandedInfoBusiness;

    public DemandedInfoController(DemandedInfoBusiness demandedInfoBusiness) {
        this.demandedInfoBusiness = demandedInfoBusiness;
    }

    /**
     * 需求申请
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody DemandedInfoInsertVm model, HttpServletRequest request) throws Exception {
        demandedInfoBusiness.insert(model, request);
        return ResultUtil.success("");
    }

    /**
     * 删除需求
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) throws DoValidException {
        demandedInfoBusiness.delete(id, request);
        return ResultUtil.success("");
    }

    /**
     * 需求详情
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findById")
    public Result<DemandedInfoVm> find(String id) throws DoValidException {
        return ResultUtil.success("", demandedInfoBusiness.find(id));
    }

    /**
     * 我的需求
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findAll")
    public Result<Page<DemandedInfoEntity>> findAll(DemandedInfoFindAllVm model, HttpServletRequest request) throws Exception{
        return ResultUtil.success("", demandedInfoBusiness.findAll(model, request));
    }

    /**
     * 我的待办-需求申请
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findAnalysis")
    public Result<DemandedInfoAnalysisVm> findAnalysis(HttpServletRequest request) throws DoValidException {
        return ResultUtil.success("", demandedInfoBusiness.findAnalysis(request));
    }
}
