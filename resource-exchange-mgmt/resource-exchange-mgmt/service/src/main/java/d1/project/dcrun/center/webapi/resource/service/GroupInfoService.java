package d1.project.dcrun.center.webapi.resource.service;

import d1.project.dcrun.center.webapi.resource.dao.GroupInfoDao;
import d1.project.dcrun.center.webapi.resource.entity.GroupInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author baozh
 */
@Service
public class GroupInfoService {
    private final GroupInfoDao groupInfoDao;

    public GroupInfoService(GroupInfoDao groupInfoDao) {
        this.groupInfoDao = groupInfoDao;
    }

    public List<GroupInfo> findAllByTypeOrderByUpdateTimeAsc(String type) {
        return groupInfoDao.findAllByTypeOrderByUpdateTimeAsc(type);
    }

    public boolean existsAllByParentIdAndName(String parentId, String name) {
        return groupInfoDao.existsAllByParentIdAndName(parentId, name);
    }

    public boolean existsAllByTypeAndName(String type, String name){
        return groupInfoDao.existsAllByTypeAndName(type, name);
    }

    public boolean existsAllByParentIdAndNameAndIdNot(String parentId, String name, String id) {
        return groupInfoDao.existsAllByParentIdAndNameAndIdNot(parentId, name, id);
    }

    public void insert(GroupInfo groupInfo) {
        groupInfoDao.save(groupInfo);
    }

    public void delete(String id) {
        groupInfoDao.deleteById(id);
    }

    public GroupInfo findById(String id) {
        Optional<GroupInfo> data = groupInfoDao.findById(id);
        return data.orElse(null);
    }

    public List<GroupInfo> findAllByParentId(String parentId) {
        return groupInfoDao.findAllByParentId(parentId);
    }
}
