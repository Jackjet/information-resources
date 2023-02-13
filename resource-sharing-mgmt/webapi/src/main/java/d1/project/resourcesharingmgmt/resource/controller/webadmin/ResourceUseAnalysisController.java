package d1.project.resourcesharingmgmt.resource.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.business.ResourceUseInfoBusiness;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo.ResourceByMonthVm;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo.ResourceUseCountAnalysisVm;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo.ResourceUserByTypeVm;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 管理端--资源使用汇总
 * @author maoyuying
 */
@Auth("webadmin")
@RestController("/webadmin/resourceUseAnalysis")
@RequestMapping("/webadmin/resourceUseAnalysis")
public class ResourceUseAnalysisController {
    private final ResourceUseInfoBusiness resourceUseInfoBusiness;

    public ResourceUseAnalysisController(ResourceUseInfoBusiness resourceUseInfoBusiness) {
        this.resourceUseInfoBusiness = resourceUseInfoBusiness;
    }

    /**
     * 资源使用汇总-统计数量
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getResourceUseCountAnalysis")
    public Result<ResourceUseCountAnalysisVm> getResourceUseCountAnalysis(HttpServletRequest request) throws Exception {
        return ResultUtil.success("",resourceUseInfoBusiness.getResourceUseCountAnalysis(request));
    }

    /**
     * 资源使用汇总-资源类型
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getResourceUseCountByType")
    public Result<ResourceUserByTypeVm> getResourceUseCountByType(HttpServletRequest request) throws Exception {
        return ResultUtil.success("",resourceUseInfoBusiness.getResourceUseCountByType(request));
    }

    /**
     * 资源使用汇总-半年申请趋势
     * @return
     * @throws Exception
     */

    @GetMapping(value = "/getResourceUseByMonth")
    public Result<List<ResourceByMonthVm>> getResourceUseByMonth(HttpServletRequest request) throws Exception {
        return ResultUtil.success("",resourceUseInfoBusiness.getResourceUseByMonth(request));
    }

    /**
     * 资源使用汇总-通过率
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getResourceUseByPassRate")
    public Result<String> getResourceUseByPassRate(HttpServletRequest request) throws Exception {
        return ResultUtil.success("",resourceUseInfoBusiness.getResourceUseByPassRate(request));
    }
}
