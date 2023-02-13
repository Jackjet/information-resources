package d1.project.d1project.apidesign.service;

import d1.project.d1project.apidesign.dao.ApiDevelopmentDao;
import d1.project.d1project.apidesign.entity.ApiDevelopment;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

/**
 * @author baozh
 */
@Service
public class ApiDevelopmentService {
    private final ApiDevelopmentDao apiDevelopmentDao;

    public ApiDevelopmentService(ApiDevelopmentDao apiDevelopmentDao) {
        this.apiDevelopmentDao = apiDevelopmentDao;
    }

    public ApiDevelopment findById(String id) {
        Optional<ApiDevelopment> data = apiDevelopmentDao.findById(id);
        return data.orElse(null);
    }

    public ApiDevelopment findFirstByApiId(String apiId) {
        return apiDevelopmentDao.findFirstByApiDesignId(apiId);
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteAll(String apiDesignId) {
        apiDevelopmentDao.deleteAllByApiDesignId(apiDesignId);
    }

    public void insert(ApiDevelopment apiDevelopment) {
        apiDevelopmentDao.save(apiDevelopment);
    }
}
