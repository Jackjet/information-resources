package d1.project.api.integration.apimanage.view.service;

import d1.project.api.integration.apimanage.view.dao.KongApiViewDao;
import d1.project.api.integration.apimanage.view.entity.KongApiView;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author libin
 */
@Service
public class KongApiViewService {
    private final KongApiViewDao kongApiViewDao;

    public KongApiViewService(KongApiViewDao kongApiViewDao) {
        this.kongApiViewDao = kongApiViewDao;
    }

    public List<KongApiView> findAllByApiIds (List<String> apiIds){
        return kongApiViewDao.findAllByApiIdIn(apiIds);
    }
}
