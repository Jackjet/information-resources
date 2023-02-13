package d1.project.resourcesharingmgmt.resource.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.business.DataAnalysisBusiness;
import d1.project.resourcesharingmgmt.resource.model.DemandedInfo.DemandedAnalysisVm;
import d1.project.resourcesharingmgmt.resource.model.DemandedInfo.DemandedByReasonVm;
import d1.project.resourcesharingmgmt.resource.model.DemandedInfo.DemandedByRequestTypeVm;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 管理端--需求汇总
 * @author maoyuying
 */
@Auth("webadmin")
@RestController("/webadmin/demandedAnalysis")
@RequestMapping("/webadmin/demandedAnalysis")
public class DemandedAnalysisController {
    private final DataAnalysisBusiness dataAnalysisBusiness;

    public DemandedAnalysisController(DataAnalysisBusiness dataAnalysisBusiness) {
        this.dataAnalysisBusiness = dataAnalysisBusiness;
    }

    /**
     * 需求汇总-统计数量
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getDemandCountAnalysis")
    public Result<DemandedAnalysisVm> getDemandCountAnalysis(HttpServletRequest request) throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getDemandCountAnalysis(request));
    }

    /**
     * 需求汇总-需求缘由
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getDemandByReason")
    public Result<DemandedByReasonVm> getDemandByReason(HttpServletRequest request) throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getDemandByReason(request));
    }

    /**
     * 需求汇总-需求类型
     * @return
     * @throws Exception
     */

    @GetMapping(value = "/getDemandByRequestType")
    public Result<DemandedByRequestTypeVm> getDemandByRequestType(HttpServletRequest request) throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getDemandByRequestType(request));
    }

    /**
     * 需求汇总-通过率
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getDemandByPassRate")
    public Result<String> getDemandByPassRate(HttpServletRequest request) throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getDemandByPassRate(request));
    }
}
