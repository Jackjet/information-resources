package d1.project.api.integration.apimanage.service;

import d1.project.api.integration.apimanage.dao.ApiBaseInfoDao;
import d1.project.api.integration.apimanage.dao.ApiGroupInfoDao;
import d1.project.api.integration.apimanage.entity.ApiGroupInfo;
import d1.project.api.integration.common.Constants;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author baozh
 */
@Service
public class ApiGroupInfoService {
    private final ApiGroupInfoDao apiGroupInfoDao;
    private final ApiBaseInfoDao apiBaseInfoDao;

    public ApiGroupInfoService(ApiGroupInfoDao apiGroupInfoDao, ApiBaseInfoDao apiBaseInfoDao) {
        this.apiGroupInfoDao = apiGroupInfoDao;
        this.apiBaseInfoDao = apiBaseInfoDao;
    }

    public List<ApiGroupInfo> findAll(Sort sort) {
        return apiGroupInfoDao.findAll(sort);
    }

    public ApiGroupInfo findById(String id) throws Exception {
        Optional<ApiGroupInfo> tempInfo = apiGroupInfoDao.findById(id);
        if (!tempInfo.isPresent()) {
            throw new Exception(Constants.DATA_NULL);
        }
        return tempInfo.get();
    }

    public boolean existsAllByName(String name) {
        return apiGroupInfoDao.existsAllByName(name);
    }

    public void saveInfo(ApiGroupInfo insertVm) {
        apiGroupInfoDao.save(insertVm);
    }

    public boolean existsAllByNameAndIdNot(String name, String id) {
        return apiGroupInfoDao.existsAllByNameAndIdNot(name, id);
    }

    public void deleteById(String id) {
        apiGroupInfoDao.deleteById(id);
    }


    public Map<String, Integer> getApiCountOfGroup() {
        List<ApiGroupInfo> apiGroupInfos = apiGroupInfoDao.findAll();

        Map<String, Integer> map = new HashMap<>();
        for (ApiGroupInfo apiGroupInfo : apiGroupInfos) {
            int count = apiBaseInfoDao.countAllByGroupId(apiGroupInfo.getId());
            map.put(apiGroupInfo.getId(), count);
        }
        return map;
    }

    public boolean existApiGroupByParentId(String parenId){
        return apiGroupInfoDao.existsByParentId(parenId);
    }
}
