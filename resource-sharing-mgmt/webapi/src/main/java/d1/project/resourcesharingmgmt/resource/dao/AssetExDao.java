package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.AssetExEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * 信息资源目录与云数据关联表
 *
 * @author zhengyang
 */
public interface AssetExDao extends JpaRepository<AssetExEntity, String>, JpaSpecificationExecutor<AssetExEntity> {

    Boolean existsByUviewIdAndType(String uviewId, int type);

    Optional<AssetExEntity> findByUviewIdAndType(String uviewId, int type);

    int countByUviewId(String uviewId);

    List<AssetExEntity> findByUviewId(String uviewId);

    Integer countByStatus(Integer status);

    Page<AssetExEntity> findAllByStatusInAndOrgIdIn(List<Integer> status, List<Integer> orgIds, Pageable pageable);

    Page<AssetExEntity> findAllByStatusIn(List<Integer> status, Pageable pageable);

    Page<AssetExEntity> findAllByStatusAndOrgIdIn(int status, List<Integer> orgIds, Pageable pageable);

    Page<AssetExEntity> findAllByStatus(int status, Pageable pageable);
}
