package d1.project.d1project.apidesign.service;

import d1.project.d1project.apidesign.dao.ApiDeploymentDao;
import d1.project.d1project.apidesign.entity.ApiDeployment;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author baozh
 */
@Service
public class ApiDeploymentService {
    private final ApiDeploymentDao apiDeploymentDao;

    public ApiDeploymentService(ApiDeploymentDao apiDeploymentDao) {
        this.apiDeploymentDao = apiDeploymentDao;
    }

    public ApiDeployment findById(String id) {
        Optional<ApiDeployment> data = apiDeploymentDao.findById(id);
        return data.orElse(null);
    }

    public List<ApiDeployment> findAllApiDeploymentList(String apiDesignId){
        return apiDeploymentDao.findAllByApiDesignId(apiDesignId);
    }

    public void insert(ApiDeployment apiDeployment) {
        apiDeploymentDao.save(apiDeployment);
    }

    public void delete(String id) {
        apiDeploymentDao.deleteById(id);
    }

    public boolean existsAllByApiDesignId(String apiId) {
        return apiDeploymentDao.existsAllByApiDesignId(apiId);
    }

    public boolean existsAllByApiDesignIdAndContainerId(String apiId, String containerId) {
        return apiDeploymentDao.existsAllByApiDesignIdAndContainerId(apiId, containerId);
    }

    public ApiDeployment findFirstByApiDesignIdAndContainerId(String apiId, String containerId) {
        return apiDeploymentDao.findFirstByApiDesignIdAndContainerId(apiId, containerId);
    }

    public List<ApiDeployment> findAllByContainerId(String containerId) {
        return apiDeploymentDao.findAllByContainerId(containerId);
    }
}
