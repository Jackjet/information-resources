package d1.project.tangshan.operation.manage.dao;

import d1.project.tangshan.operation.manage.entity.ModuleMonitorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author Buter
 */
public interface ModuleMonitorDao extends JpaRepository<ModuleMonitorEntity, String>, JpaSpecificationExecutor<ModuleMonitorEntity> {
    ModuleMonitorEntity findFirstByTypeAndNodeId(String type, String nodeId);

    ModuleMonitorEntity findFirstByTypeAndNodeIdAndSysId(String type, String nodeId, String sysId);

    Boolean existsByAlarmWayAndType(String alarmWay,String type);
    Boolean existsByAlarmWay(String alarmWay);

    List<ModuleMonitorEntity> findAllByTypeAndNodeId(String type, String nodeId);
    List<ModuleMonitorEntity> findAllByTypeAndNodeIdAndSysId(String type, String nodeId, String sysId);

}
