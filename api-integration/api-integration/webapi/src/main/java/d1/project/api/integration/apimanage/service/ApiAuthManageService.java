package d1.project.api.integration.apimanage.service;

import d1.project.api.integration.apimanage.dao.ApiAuthManageDao;
import d1.project.api.integration.apimanage.entity.ApiAuthManage;
import d1.project.api.integration.common.service.IApiAuthManageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author baozh
 */
@Service
public class ApiAuthManageService implements IApiAuthManageService {
    private final ApiAuthManageDao apiAuthManageDao;

    public ApiAuthManageService(ApiAuthManageDao apiAuthManageDao) {
        this.apiAuthManageDao = apiAuthManageDao;
    }

    @Override
    public void saveInfo(ApiAuthManage insertVm) {
        apiAuthManageDao.save(insertVm);
    }

    public boolean existsAllByApiId(String apiId) {
        return apiAuthManageDao.existsAllByApiId(apiId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(List<String> ids) {
        apiAuthManageDao.deleteAllByIdIn(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAllByAppIdIn(List<String> appIds) {
        apiAuthManageDao.deleteAllByAppIdIn(appIds);
    }

    public void deleteAllByApiId(String apiId) {
        apiAuthManageDao.deleteAllByApiId(apiId);
    }

    public Long count() {
        return apiAuthManageDao.count();
    }

    @Override
    public boolean existsAllByAppId(List<String> appIdList) {
        return apiAuthManageDao.existsAllByAppIdIn(appIdList);
    }

    @Override
    public boolean existsAllByAppId(String appId) {
        return apiAuthManageDao.existsAllByAppId(appId);
    }

    public ApiAuthManage findFirstByAppIdAndApiId(String appId, String apiId) {
        return apiAuthManageDao.findFirstByAppIdAndApiId(appId, apiId);
    }

    @Override
    public ApiAuthManage findFirstByApiIdAndAppId(String apiId, String appId){
        return apiAuthManageDao.findFirstByApiIdAndAppId(apiId,appId);
    }

    public void batchSave(List<ApiAuthManage> apiAuthManages){
        this.apiAuthManageDao.saveAll(apiAuthManages);
    }

    public List<ApiAuthManage> findAllByIds(List<String> ids){
        return apiAuthManageDao.findAllById(ids);
    }

    @Override
    public void deleteAllByAppId(String appId) {
        apiAuthManageDao.deleteAllByAppId(appId);
    }
}
