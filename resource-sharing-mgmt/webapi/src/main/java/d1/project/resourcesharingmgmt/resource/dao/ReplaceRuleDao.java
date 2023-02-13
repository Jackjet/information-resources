package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.ReplaceRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 脱敏规则
 *
 * @author zhengyang
 * @date 2021-11-05 15:34
 */
public interface ReplaceRuleDao extends JpaRepository<ReplaceRuleEntity, String>, JpaSpecificationExecutor<ReplaceRuleEntity> {
    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, String id);
}
