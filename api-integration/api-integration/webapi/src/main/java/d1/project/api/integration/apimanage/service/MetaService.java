package d1.project.api.integration.apimanage.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.SpecificationCondition;
import d1.project.api.integration.apianalysis.view.entity.AppAnalysisList;
import d1.project.api.integration.apimanage.dao.MetaDao;
import d1.project.api.integration.apimanage.entity.Meta;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MetaService {
    private final MetaDao metaDao;

    public MetaService(MetaDao metaDao) {
        this.metaDao = metaDao;
    }

    public List<Meta> findAllByKeyAndValue(String key,String value) throws Exception {
        JSONObject param = new JSONObject();
        param.put("key",key);
        param.put("value",value);

        SpecificationBuilder<Meta> builder = new SpecificationBuilder();
        Specification<Meta> specification = builder.init(param)
                .sContain("key", "key")
                .sContain("value", "value")
                .dOrder("seq").build();
        return metaDao.findAll(specification);
    }

    public void deleteByAssociationsId(String associationsId){
        List<Meta> metas = metaDao.findAllByAssociationsId(associationsId);
        metaDao.deleteAll(metas);
    }

    public void saveAll(List<Meta> data) {
        metaDao.saveAll(data);
    }

    public List<Meta> findAllByAssociationsId(String associationsId) {
        return metaDao.findAllByAssociationsId(associationsId);
    }
}
