package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.KeywordSearchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;


/**
 * 关键字搜索记录
 *
 * @author zhengyang
 */
public interface KeywordSearchDao extends JpaRepository<KeywordSearchEntity, String>, JpaSpecificationExecutor<KeywordSearchEntity> {
    Optional<KeywordSearchEntity> findByKeyword(String keyword);
}
