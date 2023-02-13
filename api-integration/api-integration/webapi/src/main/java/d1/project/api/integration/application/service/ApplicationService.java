package d1.project.api.integration.application.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.api.integration.application.dao.ApplicationDao;
import d1.project.api.integration.application.dao.ApplicationViewDao;
import d1.project.api.integration.application.entity.Application;
import d1.project.api.integration.application.model.ApplicationView;
import d1.project.api.integration.common.service.IApplicationService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService implements IApplicationService {
    private final ApplicationDao applicationDao;
    private final ApplicationViewDao applicationViewDao;

    public ApplicationService(ApplicationDao applicationDao, ApplicationViewDao applicationViewDao) {
        this.applicationDao = applicationDao;
        this.applicationViewDao = applicationViewDao;
    }

    public Page<ApplicationView> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<ApplicationView> builder = new SpecificationBuilder<>();
        Specification<ApplicationView> specification = builder.init(params)
                .sContain("name", "name")
                .sContain("createByName", "createByName")
                .sEqual("container", "container")
                .dOrder("createTime").build();
        return applicationViewDao.findAll(specification, builder.getPageable());
    }

    public Boolean existByName(String name) {
        return this.applicationDao.existsByName(name);
    }

    public Boolean existByNameAndIdNot(String name, String id) {
        return this.applicationDao.existsByNameAndIdNot(name, id);
    }

    @Override
    public void save(Application application){
        this.applicationDao.save(application);
    }

    @Override
    public Application findById(String id) throws Exception {
        return applicationDao.findById(id).orElseThrow(() -> new DoValidException("应用信息不存在"));
    }

    public void delete(Application application) throws Exception {
        this.applicationDao.delete(application);
    }

    public List<ApplicationView> findAllByNameAndContainer(JSONObject params) throws Exception {
        String name = params.getString("name");
        String container = params.getString("container");
        return applicationViewDao.findAllByNameContainsAndContainerContains(name, container);
    }

    public Long count() {
        return applicationDao.count();
    }

    public List<Application> findByIds(List<String> ids) {
        return this.applicationDao.findAllByIdIn(ids);
    }
}
