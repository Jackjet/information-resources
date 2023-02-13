package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.MyFocusInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * 我的关注
 *
 * @author zhengyang
 */
public interface MyFocusInfoDao extends JpaRepository<MyFocusInfoEntity, String>, JpaSpecificationExecutor<MyFocusInfoEntity> {
    /**
     * 统计资源目录收藏量
     * @param uviewId
     * @return
     */
    long countAllByUviewId(String uviewId);

    boolean existsAllByUviewIdAndCreateById(String uviewId, String createById);

    Optional<MyFocusInfoEntity> findFirstByUviewIdAndCreateById(String uviewId, String createById);
}
