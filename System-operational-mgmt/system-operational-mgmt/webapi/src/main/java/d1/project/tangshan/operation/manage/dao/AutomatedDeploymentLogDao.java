package d1.project.tangshan.operation.manage.dao;

import d1.project.tangshan.operation.manage.entity.AutomatedDeploymentLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * @author Buter
 */
public interface AutomatedDeploymentLogDao extends JpaRepository<AutomatedDeploymentLogEntity, String>, JpaSpecificationExecutor<AutomatedDeploymentLogEntity> {

    Optional<AutomatedDeploymentLogEntity> findFirstByAutomatedDeploymentEntityId(String automatedDeploymentEntityId);
}
