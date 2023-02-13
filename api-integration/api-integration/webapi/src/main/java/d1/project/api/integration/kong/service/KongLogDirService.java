package d1.project.api.integration.kong.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.api.integration.apimanage.view.entity.SourceApiList;
import d1.project.api.integration.common.service.IKongLogDirService;
import d1.project.api.integration.kong.dao.KongLogDirDao;
import d1.project.api.integration.kong.entity.KongLogDir;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KongLogDirService implements IKongLogDirService {
    private final KongLogDirDao kongLogDirDao;

    public KongLogDirService(KongLogDirDao kongLogDirDao) {
        this.kongLogDirDao = kongLogDirDao;
    }

    public List<KongLogDir> findByContainer(String container){
        return kongLogDirDao.findAllByContainer(container);
    }

    public void save(KongLogDir kongLogDir){
        kongLogDirDao.save(kongLogDir);
    }

    public void batchDelete(List<KongLogDir> kongLogDirs){
        kongLogDirDao.deleteAll(kongLogDirs);
    }

    @Override
    public List<KongLogDir> findAll(JSONObject param) throws Exception {
        SpecificationBuilder<KongLogDir> builder = new SpecificationBuilder<>();
        Specification<KongLogDir> specification = builder.init(param)
                .sEqual("container", "container")
                .tBetween("createTime","startTime","endTime")
                .dOrder("createTime").build();
        return kongLogDirDao.findAll(specification);
    }
}
