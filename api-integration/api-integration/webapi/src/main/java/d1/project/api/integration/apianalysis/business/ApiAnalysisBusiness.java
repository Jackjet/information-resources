package d1.project.api.integration.apianalysis.business;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.api.integration.apianalysis.entity.ApiLogRecord;
import d1.project.api.integration.apianalysis.model.*;
import d1.project.api.integration.apianalysis.service.ApiLogRecordService;
import d1.project.api.integration.apianalysis.view.entity.*;
import d1.project.api.integration.apianalysis.view.service.*;
import d1.project.api.integration.apimanage.entity.ApiBaseInfo;
import d1.project.api.integration.apimanage.entity.KongApi;
import d1.project.api.integration.apimanage.service.KongApiService;
import d1.project.api.integration.application.entity.Application;
import d1.project.api.integration.application.entity.NormalConsumer;
import d1.project.api.integration.application.service.ApplicationService;
import d1.project.api.integration.application.service.NormalConsumerService;
import d1.project.api.integration.common.utils.BaseUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author baozh
 */
@Service
public class ApiAnalysisBusiness {
    private final ApiAnalysisListService apiAnalysisListService;
    private final AppAnalysisListService appAnalysisListService;
    private final ApiLogRecordService apiLogRecordService;
    private final AppApiListService appApiListService;
    private final ApiAppListService apiAppListService;
    private final ApiLogRecordListService apiLogRecordListService;
    private final ApplicationService applicationService;
    private final KongApiService kongApiService;
    private final NormalConsumerService normalConsumerService;

    public ApiAnalysisBusiness(ApiAnalysisListService apiAnalysisListService, AppAnalysisListService appAnalysisListService, ApiLogRecordService apiLogRecordService, AppApiListService appApiListService, ApiAppListService apiAppListService, ApiLogRecordListService apiLogRecordListService, ApplicationService applicationService, KongApiService kongApiService, NormalConsumerService normalConsumerService) {
        this.apiAnalysisListService = apiAnalysisListService;
        this.appAnalysisListService = appAnalysisListService;
        this.apiLogRecordService = apiLogRecordService;
        this.appApiListService = appApiListService;
        this.apiAppListService = apiAppListService;
        this.apiLogRecordListService = apiLogRecordListService;
        this.applicationService = applicationService;
        this.kongApiService = kongApiService;
        this.normalConsumerService = normalConsumerService;
    }

    /**
     * ??????API????????????
     *
     * @param params ????????????
     * @return ????????????
     * @throws Exception ??????????????????
     */
    public Page<ApiAnalysisList> findApiList(JSONObject params) throws Exception {
        return apiAnalysisListService.findAll(params);
    }

    /**
     * ??????APP????????????
     *
     * @param params ????????????
     * @return ????????????
     * @throws Exception ??????????????????
     */
    public Page<AppAnalysisList> findAppList(JSONObject params) throws Exception {
        return appAnalysisListService.findAll(params);
    }

    /**
     * ????????????
     *
     * @param params ????????????
     * @return ApiLogRecordList
     * @throws Exception ??????????????????
     */
    public Page<ApiLogRecordList> findLogRecordList(JSONObject params) throws Exception {
        return apiLogRecordListService.findAll(params);
    }

    /**
     * ???????????????
     *
     * @param findVm ????????????
     * @return ????????????
     * @throws Exception ????????????
     */
    public List<DateNumVm> statistic(ApiStatistic findVm) throws Exception {
        Integer type = findVm.getType();
        TimeVm timeVm = getFindTime(findVm.getStartTime(), findVm.getEndTime());
        int timeType = timeType(timeVm);
        String id = findVm.getId();
        List<DateNumVm> list;
        if (timeType == 1) {
            if (type.equals(1)) {
                list = apiLogRecordService.sumByApiIdForDay(id, timeVm.getStartTime(), timeVm.getEndTime());
            } else {
                list = apiLogRecordService.sumByAppIdForDay(id, timeVm.getStartTime(), timeVm.getEndTime());
            }
        } else {
            if (type.equals(1)) {
                list = apiLogRecordService.sumByApiIdForHours(id, timeVm.getStartTime(), timeVm.getEndTime());
            } else {
                list = apiLogRecordService.sumByAppIdForHours(id, timeVm.getStartTime(), timeVm.getEndTime());
            }
        }
        return list;
    }

    /**
     * ??????APP????????????API??????
     *
     * @param params ????????????
     * @return ????????????
     * @throws Exception ??????????????????
     */
    public Page<AppApiList> findAuthApiList(JSONObject params) throws Exception {
        return appApiListService.findAll(params);
    }

    /**
     * ??????API????????????APP??????
     *
     * @param params ????????????
     * @return ????????????
     * @throws Exception ??????????????????
     */
    public Page<ApiAppList> findAuthAppList(JSONObject params) throws Exception {
        return apiAppListService.findAll(params);
    }

    /**
     * API????????????
     *
     * @param findVm ????????????
     * @return ????????????
     * @throws Exception ??????????????????
     */
    public ApiVisitNumVm apiVisitNum(FindVisitNumVm findVm) throws Exception {
        TimeVm timeVm = get24FindTime(findVm.getStartTime(), findVm.getEndTime());
        String apiId = findVm.getApiId();
        String appId = findVm.getAppId();
        int timeType = timeType(timeVm);
        String code = "200";
        List<DateNumVm> totalNum;
        List<DateNumVm> failNum;
        if (timeType == 1) {
            totalNum = apiLogRecordService.sumByApiIdAndAppIdForDay(apiId, appId, timeVm.getStartTime(), timeVm.getEndTime());
            failNum = apiLogRecordService.sumFailByApiIdAndAppIdForDay(apiId, appId, timeVm.getStartTime(), timeVm.getEndTime(), code);
        } else {
            totalNum = apiLogRecordService.sumByApiIdAndAppIdForHours(apiId, appId, timeVm.getStartTime(), timeVm.getEndTime());
            failNum = apiLogRecordService.sumFailByApiIdAndAppIdForHours(apiId, appId, timeVm.getStartTime(), timeVm.getEndTime(), code);
        }
        ApiVisitNumVm apiVisitNumVm = new ApiVisitNumVm();
        apiVisitNumVm.setTotalNum(totalNum);
        apiVisitNumVm.setFailNum(failNum);
        return apiVisitNumVm;
    }


    /**
     * API???????????? ??????IP????????????
     *
     * @param findVm ????????????
     * @return ????????????
     * @throws Exception ??????????????????
     */
    public List<NameNumVm> ipVisitNum(FindVisitNumVm findVm) throws Exception {
        TimeVm timeVm = get24FindTime(findVm.getStartTime(), findVm.getEndTime());
        String apiId = findVm.getApiId();
        String appId = findVm.getAppId();
        return apiLogRecordService.sumIpByApiIdAndAppId(apiId, appId, timeVm.getStartTime(), timeVm.getEndTime());
    }

    /**
     * API???????????? ??????CODE????????????
     *
     * @param findVm ????????????
     * @return ????????????
     * @throws Exception ??????????????????
     */
    public CodeVm codeVisitNum(FindVisitNumVm findVm) throws Exception {
        TimeVm timeVm = getFindTime(findVm.getStartTime(), findVm.getEndTime());
        String apiId = findVm.getApiId();
        String appId = findVm.getAppId();
        String success = "200";
        List<NameNumVm> codeList = apiLogRecordService.sumCodeByApiIdAndAppId(apiId, appId, timeVm.getStartTime(), timeVm.getEndTime());
        VisitResultVm visitResultVm = new VisitResultVm();
        if (codeList.size() > 0) {
            long successNum = 0L;
            long failNum = 0L;
            long totalNum;
            for (NameNumVm nameNumVm : codeList) {
                String code = nameNumVm.getName();
                if (success.equals(code)) {
                    successNum += nameNumVm.getNum();
                } else {
                    failNum += nameNumVm.getNum();
                }
            }
            totalNum = successNum + failNum;
            visitResultVm.setSuccessNum(successNum);
            visitResultVm.setFailNum(failNum);
            visitResultVm.setTotalNum(totalNum);
        }
        CodeVm codeVm = new CodeVm();
        codeVm.setCodeNum(codeList);
        codeVm.setVisitNum(visitResultVm);
        return codeVm;
    }

    /**
     * API???????????? ??????????????????????????????
     *
     * @param findVm ????????????
     * @return ????????????
     * @throws Exception ??????????????????
     */
    public DurationVm durationNum(FindVisitNumVm findVm) throws Exception {
        TimeVm timeVm = getFindTime(findVm.getStartTime(), findVm.getEndTime());
        String apiId = findVm.getApiId();
        String appId = findVm.getAppId();
        List<NameNumVm> durationNum = apiLogRecordService.sumDurationByApiIdAndAppId(apiId, appId, timeVm.getStartTime(), timeVm.getEndTime());
        List<AvgDurationVm> avgList = apiLogRecordService.getAvgMaxMinByApiIdAndAppId(apiId, appId, timeVm.getStartTime(), timeVm.getEndTime());
        DurationVm durationVm = new DurationVm();
        if (durationNum.size() > 0) {
            durationVm.setDurationNum(durationNum);
        }
        if (avgList.size() > 0) {
            durationVm.setAvg(avgList.get(0));
        }
        return durationVm;
    }


    /**
     * ????????????/????????????
     *
     * @param id   ??????ID
     * @param type ??????????????????
     * @return ????????????
     * @throws Exception ??????????????????
     */
    public String getContent(String id, Integer type) throws Exception {
        ApiLogRecord apiLogRecord = apiLogRecordService.findById(id);
        String msg;
        switch (type) {
            case 0:
                msg = apiLogRecord.getRequestContent();
                break;
            case 1:
                msg = apiLogRecord.getResponseContent();
                break;
            default:
                msg = "???????????????";
        }
        return msg;
    }

    public void insertApiLogRecord(JSONObject info,String ip) throws Exception {
        JSONObject route = info.getJSONObject("route");
        JSONObject consumer = info.getJSONObject("consumer");
        String clientIp = info.getString("client_ip");
        JSONObject request = info.getJSONObject("request");
        Long startedAt = info.getLong("started_at");
        JSONObject response = info.getJSONObject("response");
        JSONObject latencies = info.getJSONObject("latencies");

        ApiLogRecord apiLogRecord = new ApiLogRecord();
        apiLogRecord.setId(BaseUtils.generate32Id());
        apiLogRecord.setContainer(ip);
        if (!StringUtils.isEmpty(route.getString("id"))) {
            KongApi kongApi = this.kongApiService.findByRouteId(route.getString("id"));
            apiLogRecord.setApiId(kongApi.getApiId());
        }

        if (!StringUtils.isEmpty(consumer.getString("id"))) {
            NormalConsumer normalConsumer = normalConsumerService.findByCounsumerId(consumer.getString("id"));
            if(normalConsumer == null){
                throw new DoValidException("?????????????????????");
            }
            Application application = this.applicationService.findById(normalConsumer.getAppid());
            apiLogRecord.setAppId(application.getId());
        }

        apiLogRecord.setRequestIp(clientIp);
        apiLogRecord.setRequestMethod(request.getString("method"));

        apiLogRecord.setResponseTime(Calendar.getInstance());

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startedAt);
        apiLogRecord.setRequestTime(calendar);

        apiLogRecord.setResponseCode(response.getString("status"));

        Long proxy = latencies.getLong("proxy");
        if (proxy < 0) {
            proxy = 0L;
        }

        apiLogRecord.setDuration(proxy);

        Integer durationType = 0;
        if (proxy / 100 == 0) {
            durationType = 0;
        } else if (proxy / 1000 == 0) {
            durationType = 1;
        } else if (proxy / 10000 == 0) {
            durationType = 2;
        } else if (proxy / 100000 == 0) {
            durationType = 3;
        } else if (proxy / 100000 == 0) {
            durationType = 3;
        } else {
            durationType = 4;
        }

        apiLogRecord.setDurationType(durationType);

        if ("200".equals(apiLogRecord.getResponseCode())) {
            apiLogRecord.setStatus(1);
        } else {
            apiLogRecord.setStatus(0);
        }

        apiLogRecord.setRequestContent(request.toJSONString());
        apiLogRecord.setResponseContent(response.toJSONString());

        this.apiLogRecordService.save(apiLogRecord);
    }

    /**
     * ???????????????
     *
     * @param startTime ????????????
     * @param endTime   ????????????
     * @return ??????
     * @throws Exception ??????????????????
     */
    private TimeVm getFindTime(String startTime, String endTime) throws Exception {
        TimeVm timeVm = new TimeVm();
        if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
            Calendar st = Calendar.getInstance();
            Calendar et = Calendar.getInstance();
            et.set(Calendar.DATE, et.get(Calendar.DATE) - 7);
            timeVm.setStartTime(et);
            timeVm.setEndTime(st);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date tempSt = sdf.parse(startTime);
            Date tempEt = sdf.parse(endTime);
            Calendar ca = Calendar.getInstance();
            ca.setTime(tempSt);
            timeVm.setStartTime(ca);
            Calendar ca1 = Calendar.getInstance();
            ca1.setTime(tempEt);
            timeVm.setEndTime(ca1);
        }
        return timeVm;
    }

    /**
     * ???????????????
     *
     * @param startTime ????????????
     * @param endTime   ????????????
     * @return ??????
     * @throws Exception ??????????????????
     */
    private TimeVm get24FindTime(String startTime, String endTime) throws Exception {
        TimeVm timeVm = new TimeVm();
        if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
            Calendar st = Calendar.getInstance();
            Calendar et = Calendar.getInstance();
            et.add(Calendar.DATE, -1);
            timeVm.setStartTime(et);
            timeVm.setEndTime(st);
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date tempSt = sdf.parse(startTime);
            Date tempEt = sdf.parse(endTime);
            Calendar ca = Calendar.getInstance();
            ca.setTime(tempSt);
            timeVm.setStartTime(ca);
            Calendar ca1 = Calendar.getInstance();
            ca1.setTime(tempEt);
            timeVm.setEndTime(ca1);
        }
        System.out.println(timeVm.getStartTime());
        System.out.println(timeVm.getEndTime());
        return timeVm;
    }

    /**
     * ?????????????????????????????????/?????????
     *
     * @param timeVm ??????
     * @return ??????
     */
    private int timeType(TimeVm timeVm) {
        Calendar ca1 = timeVm.getStartTime();
        Calendar ca2 = timeVm.getEndTime();
        long time1 = ca1.getTimeInMillis();
        long time2 = ca2.getTimeInMillis();
        long resultTime = time2 - time1;
        long time = resultTime / (1000 * 60 * 60);
        long standardHours = 24L;
        if (time > standardHours) {
            return 1;
        } else {
            return 0;
        }
    }
}
