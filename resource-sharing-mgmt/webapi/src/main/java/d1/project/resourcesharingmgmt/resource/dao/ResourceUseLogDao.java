package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.ResourceUseLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 资源使用
 *
 * @author zhengyang
 */
public interface ResourceUseLogDao extends JpaRepository<ResourceUseLogEntity, String>, JpaSpecificationExecutor<ResourceUseLogEntity> {
}
