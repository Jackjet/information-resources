package d1.project.resourcesharingmgmt.system.dao;

import d1.project.resourcesharingmgmt.system.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 21:34
 */
public interface OrganizationDao extends JpaRepository<OrganizationEntity, String>, JpaSpecificationExecutor<OrganizationEntity> {
    boolean existsByNameAndParentId(String name, String parentId);

    List<OrganizationEntity> findByIdLinkLike(String idLink);

    boolean existsByNameAndParentIdAndIdNot(String name, String parentId, String id);

    boolean existsByNameAndIdNot(String name, String id);

    Optional<OrganizationEntity> findByParentIdAndSeq(String parentId, int seq);

    List<OrganizationEntity> findByIdLinkLikeOrderBySeqAsc(String idLink);

    Optional<OrganizationEntity> findFirstByParentIdOrderBySeqDesc(String parentId);

    Optional<OrganizationEntity> findByName(String name);

    List<OrganizationEntity> findByNameLinkLike(String nameLink);

    OrganizationEntity findByNameAndParentId(String name, String parentId);

    @Modifying
    @Query("delete from OrganizationEntity s where s.id in (?1)")
    void deleteBatch(List<String> ids);

    long countByParentId(String parentId);

    Optional<OrganizationEntity> findFirstByNameLike(String name);

    List<OrganizationEntity> findByLevel(int level);
}
