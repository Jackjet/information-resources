package d1.project.api.integration.system.dao;

import d1.project.api.integration.system.entity.MenuTreeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 菜单
 * d1Project
 *
 * @author kikki
 * @date 2020-09-10 14:39
 */
public interface MenuTreeDao extends JpaRepository<MenuTreeEntity, String>, JpaSpecificationExecutor<MenuTreeEntity> {
    boolean existsByParentId(String parentId);

    boolean existsByNameAndParentId(String name, String parentId);

    boolean existsByNameAndParentIdAndIdNot(String name, String parentId, String id);

    List<MenuTreeEntity> findByParentId(String parentId);

    List<MenuTreeEntity> findByParentIdOrderBySeqAsc(String parentId);

    boolean existsByNameAndIdNot(String name, String id);

    Optional<MenuTreeEntity> findByParentIdAndSeq(String parentId, int seq);

    List<MenuTreeEntity> findByIdLinkLikeOrderByLevelAsc(String idLink);

    List<MenuTreeEntity> findByIdLinkLikeOrderBySeqAsc(String idLink);

    List<MenuTreeEntity> findAllByIdInOrderBySeqAsc(Collection<String> id);

    List<MenuTreeEntity> findAllByIdInAndTypeInOrderBySeqAsc(Collection<String> id, Collection<Integer> type);

    List<MenuTreeEntity> findAllByIdInAndTypeOrderBySeqAsc(Collection<String> id, int type);

    Optional<MenuTreeEntity> findFirstByParentIdOrderBySeqDesc(String parentId);

    Optional<MenuTreeEntity> findFirstByParentId(String parentId);
}
