package d1.project.resourcesharingmgmt.resource.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.business.DataAnalysisBusiness;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo.ResourceUseInfoByProvOrgIdExport;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo.ResourceUseInfoByProvOrgIdVm;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo.ResourceUseInfoFindByProvOrgIdVm;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 首页--数据统计
 * @author maoyuying
 */
@Auth("webadmin")
@RestController("/webadmin/dataAnalysis")
@RequestMapping("/webadmin/dataAnalysis")
public class DataAnalysisController {
    private final DataAnalysisBusiness dataAnalysisBusiness;

    public DataAnalysisController(DataAnalysisBusiness dataAnalysisBusiness) {
        this.dataAnalysisBusiness = dataAnalysisBusiness;
    }

    /**
     * 需求统计
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findDemandInfo")
    public Result<String> findDemandInfo(HttpServletRequest request) throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getDemandInfoDataAnalysis(request));
    }

    /**
     * 资源使用统计
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findgetResourceUseInfo")
    public Result<String> findgetResourceUseInfo(HttpServletRequest request) throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getResourceUseDataAnalysis(request));
    }

    /**
     * 按照时间统计资源使用
     * @param typ 1:近一个月  2：近半年  3：年份
     * @return
     * @throws Exception
     */

    @GetMapping(value = "/findgetResourceUseInfoByTime")
    public Result<String> findgetResourceUseInfoByTime(String typ, HttpServletRequest request) throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getResourceUseInfoByTime(typ, request));
    }

    /**
     * 资源统计
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findgetArchBusiUviewExDataAnalysis")
    public Result<String> findgetArchBusiUviewExDataAnalysis(HttpServletRequest request) throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getArchBusiUviewExDataAnalysis(request));
    }

    /**
     * 按照部门统计挂接资源目录
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findArchBusiUviewExByProvOrgId")
    public Result<String> findArchBusiUviewExByProvOrgId() throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getArchBusiUviewExByProvOrgId());
    }

    /**
     * 资源申请方统计
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findResourceUseInfoByProvOrgId")
    public Result<ResourceUseInfoByProvOrgIdVm> findResourceUseInfoByProvOrgId(ResourceUseInfoFindByProvOrgIdVm model) throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.findResourceUseInfoByProvOrgId(model));
    }

    /**
     * 资源申请方统计导出
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/exportResourceUseInfoByProvOrgId")
    public Result<String> exportResourceUseInfoByProvOrgId(ResourceUseInfoFindByProvOrgIdVm model, HttpServletResponse response) throws Exception {
        List<ResourceUseInfoByProvOrgIdExport> allList = dataAnalysisBusiness.exportResourceUseInfoByProvOrgId(model);
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        dataAnalysisBusiness.easyExcelWrite(response, allList);
        return null;
    }
}
