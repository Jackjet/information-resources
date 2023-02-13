package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.OrgExEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author zhengyang
 */
public interface OrgExDao extends JpaRepository<OrgExEntity, String>, JpaSpecificationExecutor<OrgExEntity>{
}
