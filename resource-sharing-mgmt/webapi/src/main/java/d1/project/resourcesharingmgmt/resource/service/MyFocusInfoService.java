package d1.project.resourcesharingmgmt.resource.service;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.resourcesharingmgmt.resource.dao.MyFocusInfoDao;
import d1.project.resourcesharingmgmt.resource.entity.MyFocusInfoEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 我的关注
 *
 * @author zhengyang
 */
@Service
public class MyFocusInfoService {
    private final MyFocusInfoDao myFocusInfoDao;

    public MyFocusInfoService(MyFocusInfoDao myFocusInfoDao) {
        this.myFocusInfoDao = myFocusInfoDao;
    }

    /**
     * 详情
     *
     * @param id id
     */
    public Optional<MyFocusInfoEntity> find(String id) {
        return myFocusInfoDao.findById(id);
    }

    public void insert(MyFocusInfoEntity myFocusInfoEntity) throws DoValidException {
        myFocusInfoDao.save(myFocusInfoEntity);
    }

    public void delete(String id) throws DoValidException {
        myFocusInfoDao.deleteById(id);
    }

    public long countAllByUviewId(String uviewId) throws DoValidException {
        return myFocusInfoDao.countAllByUviewId(uviewId);
    }

    /**
     * 校验用户是否关注过该资源
     *
     * @param uviewId
     * @param createById
     * @return
     */
    public boolean existsAllByUviewIdAndCreateById(String uviewId, String createById) {
        return myFocusInfoDao.existsAllByUviewIdAndCreateById(uviewId, createById);
    }

    /**
     * 根据资源目录和关注的用户查询关注
     * @param uviewId
     * @param createById
     * @return
     */
    public Optional<MyFocusInfoEntity> findFirstByUviewIdAndCreateById(String uviewId, String createById) {
        return myFocusInfoDao.findFirstByUviewIdAndCreateById(uviewId, createById);
    }

}
