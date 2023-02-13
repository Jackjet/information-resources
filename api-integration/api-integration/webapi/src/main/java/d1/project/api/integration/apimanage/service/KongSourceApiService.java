package d1.project.api.integration.apimanage.service;

import d1.project.api.integration.apimanage.dao.KongSourceApiDao;
import d1.project.api.integration.apimanage.entity.KongSourceApi;
import d1.project.api.integration.common.service.IKongSourceApiService;
import org.springframework.stereotype.Service;

@Service
public class KongSourceApiService implements IKongSourceApiService {
    private final KongSourceApiDao kongSourceApiDao;

    public KongSourceApiService(KongSourceApiDao kongSourceApiDao) {
        this.kongSourceApiDao = kongSourceApiDao;
    }

    @Override
    public void save(KongSourceApi kongSourceApi){
        kongSourceApiDao.save(kongSourceApi);
    }

    @Override
    public KongSourceApi findByApiId(String sourceId){
        return kongSourceApiDao.findFirstBySourceId(sourceId);
    }

    public void delete(KongSourceApi kongSourceApi){
        kongSourceApiDao.delete(kongSourceApi);
    }
}
