package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.CorrectionEntity;
import d1.project.resourcesharingmgmt.resource.entity.UserSystemSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * d1project
 *
 * @author zhengyang
 * @date 2021-11-05 15:34
 */
public interface CorrectionDao extends JpaRepository<CorrectionEntity, String>, JpaSpecificationExecutor<CorrectionEntity> {
    boolean existsByUviewIdAndCreateById(String uviewId, String createById);
}
