package d1.project.tangshan.operation.manage.dao.operations.log;

import d1.project.tangshan.operation.manage.entity.operations.log.AuditLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author lin
 */
public interface AuditLogDao extends JpaRepository<AuditLog, String>, JpaSpecificationExecutor<AuditLog> {
    Integer countAllByOperatorAndLevel(String operator, String level);

    Integer countAllByOperatorAndLevelAndResultAndCreateTimeBetween(String operator, String level, String result, Calendar startTime,Calendar endTime);

    @Query(value = "select count(id) as number ,string_agg(distinct operand,',')as name from d1_audit_log where level = '1' and result ='1' and create_time between ?1 and ?2 group by operand", nativeQuery = true)
    List<Map<String,Object>> countSystemNum(Calendar startTime, Calendar endTime);
}
