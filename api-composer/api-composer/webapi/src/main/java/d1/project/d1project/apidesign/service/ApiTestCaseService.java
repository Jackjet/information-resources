package d1.project.d1project.apidesign.service;

import d1.project.d1project.apidesign.dao.ApiTestCaseDao;
import d1.project.d1project.apidesign.entity.ApiTestCase;
import d1.project.d1project.apidesign.model.ApiTestCaseVm;
import d1.project.d1project.common.Constants;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

/**
 * @author baozh
 */
@Service
public class ApiTestCaseService {
    private final ApiTestCaseDao apiTestCaseDao;

    public ApiTestCaseService(ApiTestCaseDao apiTestCaseDao) {
        this.apiTestCaseDao = apiTestCaseDao;
    }

    public ApiTestCase findById(String id) throws Exception {
        Optional<ApiTestCase> tempInfo = apiTestCaseDao.findById(id);
        if (!tempInfo.isPresent()) {
            throw new Exception(Constants.DATA_NULL);
        }
        return tempInfo.get();
    }

    public void saveInfo(ApiTestCase apiTestCase) {
        apiTestCaseDao.save(apiTestCase);
    }

    public List<ApiTestCaseVm> findCaseByApiId(String apiId, Calendar st, Calendar ed) {
        return apiTestCaseDao.findCaseByApiId(apiId, st, ed);
    }

    public void deleteById(String id) {
        apiTestCaseDao.deleteById(id);
    }
}
