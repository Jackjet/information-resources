package d1.project.api.integration.apimanage.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.api.integration.apimanage.dao.ApiBaseInfoDao;
import d1.project.api.integration.apimanage.entity.ApiBaseInfo;
import d1.project.api.integration.common.Constants;
import d1.project.api.integration.common.service.IApiService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author baozh
 */
@Service
public class ApiBaseInfoService implements IApiService {

    private final ApiBaseInfoDao apiBaseInfoDao;

    public ApiBaseInfoService(ApiBaseInfoDao apiBaseInfoDao) {
        this.apiBaseInfoDao = apiBaseInfoDao;
    }

    public ApiBaseInfo findById(String id) throws Exception {
        Optional<ApiBaseInfo> info = apiBaseInfoDao.findById(id);
        if (!info.isPresent()) {
            throw new Exception(Constants.DATA_NULL);
        }
        return info.get();
    }

    @Override
    public ApiBaseInfo findFirstByName(String name) {
        return apiBaseInfoDao.findFirstByName(name);
    }

    @Override
    public boolean existsAllByName(String name) {
        return apiBaseInfoDao.existsAllByName(name);
    }

    public boolean existsAllByNameAndIdNot(String name, String id) {
        return apiBaseInfoDao.existsAllByNameAndIdNot(name, id);
    }

    @Override
    public void saveInfo(ApiBaseInfo apiBaseInfo) {
        apiBaseInfoDao.save(apiBaseInfo);
    }

    public void deleteById(String id) {
        apiBaseInfoDao.deleteById(id);
    }

    public Long count() {
        return apiBaseInfoDao.count();
    }

    public List<ApiBaseInfo> findAllByIdIn(List<String> ids) {
        return apiBaseInfoDao.findAllByIdIn(ids);
    }

    public List<ApiBaseInfo> findAllByGroupId(String groupId) {
        return apiBaseInfoDao.findAllByGroupId(groupId);
    }

    public boolean existById(String id) {
        return apiBaseInfoDao.existsById(id);
    }

    @Override
    public Page<ApiBaseInfo> findAllByContainer(JSONObject param) throws Exception {
        SpecificationBuilder<ApiBaseInfo> builder = new SpecificationBuilder();
        Specification<ApiBaseInfo> specification = builder.init(param)
                .sEqual("container", "container")
                .sContain("name", "name")
                .dOrder("updateTime").build();
        return apiBaseInfoDao.findAll(specification, builder.getPageable());
    }

    @Override
    public List<ApiBaseInfo> findAllByContainer(String container) {
        return apiBaseInfoDao.findAllByContainer(container);
    }

    public int countByGroupId(String groupId) {
        return apiBaseInfoDao.countByGroupId(groupId);
    }
}
