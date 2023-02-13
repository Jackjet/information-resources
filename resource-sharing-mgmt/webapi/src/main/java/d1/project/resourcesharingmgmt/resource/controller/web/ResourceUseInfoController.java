package d1.project.resourcesharingmgmt.resource.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.business.ResourceUseInfoBusiness;
import d1.project.resourcesharingmgmt.resource.entity.ResourceUseInfoEntity;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo.ResourceUseInfoFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo.ResourceUseInfoInsertVm;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfoAnalysisVm;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 门户端_资源使用（云接口云文件云数据）
 *
 * @author zhengyang
 */
@Auth("webadmin")
@RestController("/web/resourceUseInfo")
@RequestMapping("/web/resourceUseInfo")
public class ResourceUseInfoController {

    private final ResourceUseInfoBusiness resourceUseInfoBusiness;

    public ResourceUseInfoController(ResourceUseInfoBusiness resourceUseInfoBusiness) {
        this.resourceUseInfoBusiness = resourceUseInfoBusiness;
    }

    /**
     * 我的申请
     *
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findAll")
    public Result<Page<ResourceUseInfoEntity>> findAll(ResourceUseInfoFindAllVm model, HttpServletRequest request) throws Exception {
        return ResultUtil.success("", resourceUseInfoBusiness.findAll(model, request));
    }

    /**
     * 资源使用申请(云接口云文件云数据申请)
     *
     * @param model
     * @param request
     * @return
     * @throws DoValidException
     */
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody ResourceUseInfoInsertVm model, HttpServletRequest request) throws DoValidException {
        resourceUseInfoBusiness.insert(model, request);
        return ResultUtil.success("您已经提交申请，请等待受理");
    }

    /**
     * 删除云接口云文件云数据申请
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) throws DoValidException {
        resourceUseInfoBusiness.delete(id, request);
        return ResultUtil.success("");
    }

    /**
     * 资源使用详情
     *
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findById")
    public Result<ResourceUseInfoEntity> find(String id) throws DoValidException {
        return ResultUtil.success("", resourceUseInfoBusiness.find(id));
    }

    /**
     * 我的待办-需求使用申请
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findAnalysis")
    public Result<ResourceUseInfoAnalysisVm> findAnalysis(HttpServletRequest request) throws DoValidException {
        return ResultUtil.success("", resourceUseInfoBusiness.findAnalysis(request));
    }
}
