package d1.project.api.integration.apimanage.service;

import d1.project.api.integration.apimanage.dao.GroupApiDao;
import d1.project.api.integration.apimanage.entity.GroupApi;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author baozh
 */
@Service
public class GroupApiService {
    private final GroupApiDao groupApiDao;

    public GroupApiService(GroupApiDao groupApiDao) {
        this.groupApiDao = groupApiDao;
    }

    public boolean existsAllByGroupId(String groupId) {
        return groupApiDao.existsAllByGroupId(groupId);
    }

    public void saveInfo(GroupApi groupApi) {
        groupApiDao.save(groupApi);
    }

    public void deleteAll(List<String> ids) {
        groupApiDao.deleteAllByIdIn(ids);
    }

    public List<GroupApi> findAll(String groupId) {
        return groupApiDao.findAllByGroupId(groupId);
    }
}
