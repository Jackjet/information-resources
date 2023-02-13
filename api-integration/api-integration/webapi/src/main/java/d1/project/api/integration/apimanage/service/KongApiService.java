package d1.project.api.integration.apimanage.service;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.api.integration.apimanage.dao.KongApiDao;

import d1.project.api.integration.apimanage.entity.KongApi;
import d1.project.api.integration.common.service.IKongApiService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KongApiService implements IKongApiService {
    private final KongApiDao kongApiDao;

    public KongApiService(KongApiDao kongApiDao) {
        this.kongApiDao = kongApiDao;
    }

    public Boolean existsByApiId(String apiId) {
        return kongApiDao.existsByApiId(apiId);
    }

    @Override
    public KongApi findByApiId(String apiId) {
        return kongApiDao.findFirstByApiId(apiId);
    }

    @Override
    public void save(KongApi kongApi) {
        this.kongApiDao.save(kongApi);
    }

    public List<KongApi> findAllByApiIds(List<String> appIds) {
        return this.kongApiDao.findAllByApiIdIn(appIds);
    }

    public KongApi findByRouteId(String routeId) {
        return kongApiDao.findFirstByRouteId(routeId);
    }

    public void batchSave(List<KongApi> kongApis) {
        this.kongApiDao.saveAll(kongApis);
    }

    public void delete(KongApi kongApi){
        this.kongApiDao.delete(kongApi);
    }
}
