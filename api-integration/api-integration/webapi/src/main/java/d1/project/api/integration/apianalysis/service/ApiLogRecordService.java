package d1.project.api.integration.apianalysis.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.api.integration.apianalysis.dao.ApiLogRecordDao;
import d1.project.api.integration.apianalysis.entity.ApiLogRecord;
import d1.project.api.integration.apianalysis.model.AvgDurationVm;
import d1.project.api.integration.apianalysis.model.DateNumVm;
import d1.project.api.integration.apianalysis.model.NameNumVm;
import d1.project.api.integration.application.model.ApplicationView;
import d1.project.api.integration.common.Constants;
import d1.project.api.integration.common.service.IApiLogRecordService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author baozh
 */
@Service
public class ApiLogRecordService implements IApiLogRecordService {
    private final ApiLogRecordDao apiLogRecordDao;

    public ApiLogRecordService(ApiLogRecordDao apiLogRecordDao) {
        this.apiLogRecordDao = apiLogRecordDao;
    }

    public Page<ApiLogRecord> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<ApiLogRecord> builder = new SpecificationBuilder<>();
        Specification<ApiLogRecord> specification = builder.init(params)
                .tThanEqual("requestTime", "startTime", "yyyy-MM-dd HH:mm:ss")
                .dOrder("requestTime").build();
        return apiLogRecordDao.findAll(specification, builder.getPageable());
    }

    public List<DateNumVm> sumByApiIdForDay(String apiId, Calendar startTime, Calendar endTime) {
        return JSON.parseArray(JSON.toJSONString(apiLogRecordDao.sumByApiIdForDay(apiId, startTime, endTime)), DateNumVm.class);
    }

    public List<DateNumVm> sumByApiIdForHours(String apiId, Calendar startTime, Calendar endTime) {
        List<Map<String, Object>> list = apiLogRecordDao.sumByApiIdForHours(apiId, startTime, endTime);
        return JSON.parseArray(JSON.toJSONString(list), DateNumVm.class);
    }

    public List<DateNumVm> sumByAppIdForDay(String appId, Calendar startTime, Calendar endTime) {
        return JSON.parseArray(JSON.toJSONString(apiLogRecordDao.sumByAppIdForDay(appId, startTime, endTime)), DateNumVm.class);
    }

    public List<DateNumVm> sumByAppIdForHours(String appId, Calendar startTime, Calendar endTime) {
        return JSON.parseArray(JSON.toJSONString(apiLogRecordDao.sumByAppIdForHours(appId, startTime, endTime)), DateNumVm.class);
    }

    public List<DateNumVm> sumByApiIdAndAppIdForDay(String apiId, String appId, Calendar startTime, Calendar endTime) {
        List<Map<String, Object>> list = apiLogRecordDao.sumByApiIdAndAppIdForDay(apiId, appId, startTime, endTime);
        return JSON.parseArray(JSON.toJSONString(apiLogRecordDao.sumByApiIdAndAppIdForDay(apiId, appId, startTime, endTime)), DateNumVm.class);
    }

    public List<DateNumVm> sumFailByApiIdAndAppIdForDay(String apiId, String appId, Calendar startTime, Calendar endTime, String code) {
        return JSON.parseArray(JSON.toJSONString(apiLogRecordDao.sumFailByApiIdAndAppIdForDay(apiId, appId, startTime, endTime, code)), DateNumVm.class);
    }

    public List<DateNumVm> sumByApiIdAndAppIdForHours(String apiId, String appId, Calendar startTime, Calendar endTime) {
        return JSON.parseArray(JSON.toJSONString(apiLogRecordDao.sumByApiIdAndAppIdForHours(apiId, appId, startTime, endTime)), DateNumVm.class);
    }

    public List<DateNumVm> sumFailByApiIdAndAppIdForHours(String apiId, String appId, Calendar startTime, Calendar endTime, String code) {
        return JSON.parseArray(JSON.toJSONString(apiLogRecordDao.sumFailByApiIdAndAppIdForHours(apiId, appId, startTime, endTime, code)), DateNumVm.class);
    }

    public List<NameNumVm> sumIpByApiIdAndAppId(String apiId, String appId, Calendar startTime, Calendar endTime) {
        return JSON.parseArray(JSON.toJSONString(apiLogRecordDao.sumIpByApiIdAndAppId(apiId, appId, startTime, endTime)), NameNumVm.class);
    }

    public List<NameNumVm> sumCodeByApiIdAndAppId(String apiId, String appId, Calendar startTime, Calendar endTime) {
        return JSON.parseArray(JSON.toJSONString(apiLogRecordDao.sumCodeByApiIdAndAppId(apiId, appId, startTime, endTime)), NameNumVm.class);
    }

    public List<NameNumVm> sumDurationByApiIdAndAppId(String apiId, String appId, Calendar startTime, Calendar endTime) {
        return JSON.parseArray(JSON.toJSONString(apiLogRecordDao.sumDurationByApiIdAndAppId(apiId, appId, startTime, endTime)), NameNumVm.class);
    }

    public List<AvgDurationVm> getAvgMaxMinByApiIdAndAppId(String apiId, String appId, Calendar startTime, Calendar endTime) {
        return JSON.parseArray(JSON.toJSONString(apiLogRecordDao.getAvgMaxMinByApiIdAndAppId(apiId, appId, startTime, endTime)), AvgDurationVm.class);
    }

    public Long countAllByRequestTimeBetween(Calendar st, Calendar et) {
        return apiLogRecordDao.countAllByRequestTimeBetween(st, et);
    }

    public Long countAllByRequestTimeBetweenAndResponseCode(Calendar st, Calendar et, String code) {
        return apiLogRecordDao.countAllByRequestTimeBetweenAndResponseCode(st, et, code);
    }

    public List<NameNumVm> sumFailApi(Calendar st, Calendar et, String code) {
        return JSON.parseArray(JSON.toJSONString(apiLogRecordDao.sumFailApi(st, et, code)), NameNumVm.class);
    }

    public List<NameNumVm> sumDuration(Calendar st, Calendar et) {
        return JSON.parseArray(JSON.toJSONString(apiLogRecordDao.sumDuration(st, et)), NameNumVm.class);
    }

    public void save(ApiLogRecord apiLogRecord) {
        this.apiLogRecordDao.save(apiLogRecord);
    }

    public ApiLogRecord findById(String id) throws Exception {
        Optional<ApiLogRecord> log = apiLogRecordDao.findById(id);
        if (!log.isPresent()) {
            throw new Exception(Constants.DATA_NULL);
        }
        return log.get();
    }

    @Override
    public int countAll() {
        return apiLogRecordDao.countAll();
    }

    @Override
    public int countByStatus(Integer status) {
        return apiLogRecordDao.countByStatus(status);
    }
}
