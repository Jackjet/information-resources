package d1.project.d1project.system.dao;

import d1.project.d1project.system.entity.OrganizationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

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
    boolean existsByName(String name);
    boolean existsByParentId(String parentId);
    List<OrganizationEntity> findByIdLinkLike(String idLink);

    boolean existsByNameAndParentIdAndIdNot(String name, String parentId, String id);
    boolean existsByNameAndIdNot(String name, String id);

    List<OrganizationEntity> findByParentId(String parentId);

    Optional<OrganizationEntity> findByParentIdAndSeq(String parentId, int seq);

    List<OrganizationEntity> findByIdLinkLikeOrderByLevelAsc(String idLink);

    List<OrganizationEntity> findByIdLinkLikeOrderBySeqAsc(String idLink);

    Optional<OrganizationEntity> findFirstByParentIdOrderBySeqDesc(String parentId);

    Optional<OrganizationEntity> findByName(String name);

    Optional<OrganizationEntity> findByNameLink(String nameLink);

    List<OrganizationEntity> findByNameLinkLike(String nameLink);
}
