package d1.project.api.integration.apimanage.service;

import d1.project.api.integration.apimanage.dao.SourceApiDao;
import d1.project.api.integration.apimanage.entity.SourceApi;
import d1.project.api.integration.common.service.ISourceApiService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SourceApiService implements ISourceApiService {
    private final SourceApiDao sourceApiDao;

    public SourceApiService(SourceApiDao sourceApiDao) {
        this.sourceApiDao = sourceApiDao;
    }

    @Override
    public void save(SourceApi sourceApi){
        sourceApiDao.save(sourceApi);
    }

    @Override
    public SourceApi findFirstByApiId(String apiId){
        return sourceApiDao.findFirstByApiId(apiId);
    }

    public void deleteAllByApiId(String apiId){
        List<SourceApi> sourceApis = sourceApiDao.findAllByApiId(apiId);
        sourceApiDao.deleteAll(sourceApis);
    }

    public List<SourceApi> findAllByApiId(List<String> apiIds){
        return sourceApiDao.findAllByApiIdIn(apiIds);
    }
}
