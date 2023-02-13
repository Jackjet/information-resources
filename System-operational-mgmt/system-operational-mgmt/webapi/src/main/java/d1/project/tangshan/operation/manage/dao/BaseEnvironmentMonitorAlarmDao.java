package d1.project.tangshan.operation.manage.dao;

import d1.project.tangshan.operation.manage.entity.BaseEnvironmentMonitorAlarmEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * @author Buter
 */
public interface BaseEnvironmentMonitorAlarmDao extends JpaRepository<BaseEnvironmentMonitorAlarmEntity, String>, JpaSpecificationExecutor<BaseEnvironmentMonitorAlarmEntity> {

    Optional<BaseEnvironmentMonitorAlarmEntity> findByCreateByIdAndType(String createById, String type);
    BaseEnvironmentMonitorAlarmEntity findFirstByNodeIdAndType(String nodeId,String type);
    BaseEnvironmentMonitorAlarmEntity findFirstByNodeIdAndSystemIdAndType(String nodeId,String systemId,String type);
}
