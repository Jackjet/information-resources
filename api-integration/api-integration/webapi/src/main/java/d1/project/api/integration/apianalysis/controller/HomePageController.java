package d1.project.api.integration.apianalysis.controller;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.api.integration.apianalysis.business.HomePageBusiness;
import d1.project.api.integration.apianalysis.model.BusinessStatisticsVm;
import d1.project.api.integration.apianalysis.model.MonitoringVm;
import d1.project.api.integration.common.annotation.ApiAuth;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author baozh
 */
@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/homePage")
public class HomePageController {
    private final HomePageBusiness homePageBusiness;

    public HomePageController(HomePageBusiness homePageBusiness) {
        this.homePageBusiness = homePageBusiness;
    }

    @GetMapping(value = "/businessStatistics")
    public Result<BusinessStatisticsVm> businessStatistics() {
        try {
            return ResultUtil.success("成功", homePageBusiness.businessStatistics());
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/monitoring")
    public Result<MonitoringVm> monitoring() {
        try {
            return ResultUtil.success("成功", homePageBusiness.monitoring());
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }
}
