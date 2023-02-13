package d1.project.d1project.apidesign.service;

import d1.project.d1project.apidesign.dao.ApiTestLogDao;
import d1.project.d1project.apidesign.entity.ApiTestLog;
import d1.project.d1project.apidesign.model.ApiLogVm;
import d1.project.d1project.common.Constants;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * @author baozh
 */
@Service
public class ApiTestLogService {
    private final ApiTestLogDao apiTestLogDao;

    public ApiTestLogService(ApiTestLogDao apiTestLogDao) {
        this.apiTestLogDao = apiTestLogDao;
    }

    public ApiTestLog findById(String id) throws Exception {
        Optional<ApiTestLog> tempInfo = apiTestLogDao.findById(id);
        if (!tempInfo.isPresent()) {
            throw new Exception(Constants.DATA_NULL);
        }
        return tempInfo.get();
    }

    public List<ApiLogVm> findLogList(String apiId, Calendar startTime, Calendar endTime) {
        return apiTestLogDao.findLogByApiId(apiId, startTime, endTime);
    }

    public void saveInfo(ApiTestLog apiTestLog) {
        apiTestLogDao.save(apiTestLog);
    }

    public void deleteById(String id) {
        apiTestLogDao.deleteById(id);
    }
}
