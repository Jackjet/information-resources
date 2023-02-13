package d1.project.api.integration.apianalysis.business;

import d1.project.api.integration.apianalysis.model.BusinessStatisticsVm;
import d1.project.api.integration.apianalysis.model.MonitoringVm;
import d1.project.api.integration.apianalysis.model.NameNumVm;
import d1.project.api.integration.apianalysis.service.ApiLogRecordService;
import d1.project.api.integration.apimanage.service.ApiAuthManageService;
import d1.project.api.integration.apimanage.service.ApiBaseInfoService;
import d1.project.api.integration.application.service.ApplicationService;
import d1.project.api.integration.common.utils.BaseUtils;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

/**
 * @author baozh
 */
@Service
public class HomePageBusiness {
    private final ApiLogRecordService apiLogRecordService;
    private final ApiBaseInfoService apiBaseInfoService;
    private final ApiAuthManageService apiAuthManageService;
    private final ApplicationService applicationService;

    public HomePageBusiness(ApiLogRecordService apiLogRecordService, ApiBaseInfoService apiBaseInfoService, ApiAuthManageService apiAuthManageService, ApplicationService applicationService) {
        this.apiLogRecordService = apiLogRecordService;
        this.apiBaseInfoService = apiBaseInfoService;
        this.apiAuthManageService = apiAuthManageService;
        this.applicationService = applicationService;
    }

    /**
     * 首页业务统计数据
     *
     * @return 查询结果
     */
    public BusinessStatisticsVm businessStatistics() {
        BusinessStatisticsVm businessStatisticsVm = new BusinessStatisticsVm();
        Calendar st = BaseUtils.todayFirstTime();
        Calendar et = BaseUtils.todayLastTime();
        Long visitNum = apiLogRecordService.countAllByRequestTimeBetween(st, et);
        if (visitNum != 0L) {
            businessStatisticsVm.setTodayNum(visitNum);
            String code = "200";
            Long successNum = apiLogRecordService.countAllByRequestTimeBetweenAndResponseCode(st, et, code);
            if (successNum != 0L) {
                Double rate = ((double) successNum / (double) visitNum) * 100;
                businessStatisticsVm.setSuccessRate(rate);
            }
        }
        Long apiNum = apiBaseInfoService.count();
        if (apiNum != 0L) {
            businessStatisticsVm.setApiNum(apiNum);
        }
        Long authNum = apiAuthManageService.count();
        if (authNum != 0L) {
            businessStatisticsVm.setAuthNum(authNum);
        }
        Long appNum = applicationService.count();
        if (appNum != 0L) {
            businessStatisticsVm.setAppNum(appNum);
        }
        return businessStatisticsVm;
    }

    /**
     * API实时监控
     *
     * @return 查询结果
     */
    public MonitoringVm monitoring() {
        Calendar st = BaseUtils.todayFirstTime();
        Calendar et = BaseUtils.todayLastTime();
        String code = "200";
        MonitoringVm monitoringVm = new MonitoringVm();
        List<NameNumVm> apiFail = apiLogRecordService.sumFailApi(st, et, code);
        if (apiFail.size() > 0) {
            monitoringVm.setApiFail(apiFail);
        }
        List<NameNumVm> apiTime = apiLogRecordService.sumDuration(st, et);
        if (apiTime.size() > 0) {
            monitoringVm.setApiTime(apiTime);
        }
        return monitoringVm;
    }
}
