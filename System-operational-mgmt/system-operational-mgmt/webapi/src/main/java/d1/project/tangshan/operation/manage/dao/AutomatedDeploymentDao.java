package d1.project.tangshan.operation.manage.dao;

import d1.project.tangshan.operation.manage.entity.AutomatedDeploymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * @author Buter
 */
public interface AutomatedDeploymentDao extends JpaRepository<AutomatedDeploymentEntity, String>, JpaSpecificationExecutor<AutomatedDeploymentEntity> {

//    boolean existsByVersionNumberGreaterThanEqualAndType(String versionNumber, String type);
    boolean existsByVersionNumberGreaterThanEqualAndSysId(String versionNumber, String sysId);
    boolean existsByVersionNumberGreaterThanAndSysIdAndType(String versionNumber, String sysId, String type);
    List<AutomatedDeploymentEntity> findByVersionNumberLessThanAndTypeAndSysIdAndTypeStage(String versionNumber, String type, String sysId, String typeStage);
    AutomatedDeploymentEntity findFirstByVersionNumberLessThanAndTypeAndSysId(String versionNumber, String type, String sysId);
     boolean existsBySysId(String sysId);
     Long countBySysIdAndType(String sysId, String type);
     boolean existsBySysIdAndTypeStageInAndType(String sysId, Collection<String> typeStage, String type);


    Optional<AutomatedDeploymentEntity> findFirstBySysIdAndTypeOrderByCreateTimeDesc(String sysId, String type);

}
