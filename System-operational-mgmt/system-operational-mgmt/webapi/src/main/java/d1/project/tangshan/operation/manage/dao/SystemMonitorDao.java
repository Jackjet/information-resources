package d1.project.tangshan.operation.manage.dao;

import d1.project.tangshan.operation.manage.entity.SystemMonitorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author Buter
 */
public interface SystemMonitorDao extends JpaRepository<SystemMonitorEntity, String>, JpaSpecificationExecutor<SystemMonitorEntity> {

    @Query(value = "select * from(select data_source,memory_source,to_char( log_time, 'YYYY-MM-DD' ) as date ,ROW_NUMBER() over(partition by to_char( log_time, 'YYYY-MM-DD' ) order by log_time desc) as new_index  \n" +
            "from d1_system_monitor where system_id = ?1 and log_time >= ?2 and log_time <= ?3)a where a.new_index=1", nativeQuery = true)
    List<Map<String, Object>> findByDay(String systemId, Calendar startTime, Calendar endTime);

    SystemMonitorEntity findFirstByNodeIdAndSystemIdOrderByCreateTimeDesc(String nodeId, String systemId);
}
