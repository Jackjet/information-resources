package d1.project.resourcesharingmgmt.system.dao;

import d1.project.resourcesharingmgmt.system.entity.SystemSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 21:34
 */
public interface SystemSettingsDao extends JpaRepository<SystemSettingsEntity, String>, JpaSpecificationExecutor<SystemSettingsEntity> {
    Optional<SystemSettingsEntity> findByType(String type);
}
