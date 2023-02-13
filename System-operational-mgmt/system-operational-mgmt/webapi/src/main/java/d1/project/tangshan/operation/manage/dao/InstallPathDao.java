package d1.project.tangshan.operation.manage.dao;

import d1.project.tangshan.operation.manage.entity.InstallPathEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * @author Buter
 */
public interface InstallPathDao extends JpaRepository<InstallPathEntity, String>, JpaSpecificationExecutor<InstallPathEntity> {
    Optional<InstallPathEntity> findByNodeIdAndSysId(String nodeId, String sysId);
}
