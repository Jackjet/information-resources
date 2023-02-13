package d1.project.tangshan.operation.manage.dao.operations.log;

import d1.project.tangshan.operation.manage.entity.operations.log.OperationLog;
import d1.project.tangshan.operation.manage.model.DateAndNumber;
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
public interface OperationLogDao extends JpaRepository<OperationLog, String>, JpaSpecificationExecutor<OperationLog> {

    @Query(value = "select to_char(ope.create_time, 'YYYY-MM-DD') as date , count(ope.id) as number ,string_agg(distinct ope.source,',')as name , string_agg(distinct ope.operand,',') as module_name from (select create_time,id,source,operand from d1_operation_log where (CASE WHEN ?1 =''  THEN 1=1 ELSE source like ('%'|| ?1 || '%') END ) and (CASE WHEN ?2 ='' THEN 1=1 ELSE operand like ('%'|| ?2 || '%') END ) and create_time between ?3 and ?4 order by create_time desc) as ope  group by ope.source,ope.operand,to_char(ope.create_time, 'YYYY-MM-DD') order by ope.source,ope.operand,date desc", nativeQuery = true)
    List<Map<String,Object>> findByDay(String name, String moduleName, Calendar startTime, Calendar endTime, Pageable pageble);

    @Query(value = "select count(1) from (select count(*) from (select create_time,id,source,operand from d1_operation_log where (CASE WHEN ?1 =''   THEN 1=1 ELSE source like ('%'|| ?1 || '%') END ) and (CASE WHEN ?2 ='' THEN 1=1 ELSE operand like ('%'|| ?2 || '%') END ) and create_time between ?3 and ?4 ) as ope group by ope.source,ope.operand,to_char(ope.create_time, 'YYYY-MM-DD')) a", nativeQuery = true)
    Long countByDay(String name, String moduleName, Calendar startTime, Calendar endTime);

    @Query(value = "select to_char(ope.create_time, 'YYYY-MM') as date , count(ope.id) as number ,string_agg(distinct ope.source,',')as name , string_agg(distinct ope.operand,',') as module_name from (select create_time,id,source,operand from d1_operation_log where (CASE WHEN ?1 =''  THEN 1=1 ELSE source like ('%'|| ?1 || '%') END ) and (CASE WHEN ?2 ='' THEN 1=1 ELSE operand like ('%'|| ?2 || '%') END ) and create_time between ?3 and ?4 order by create_time desc) as ope group by ope.source,ope.operand,to_char(ope.create_time, 'YYYY-MM')", nativeQuery = true)
    List<Map<String,Object>> findByMonth(String name, String moduleName, Calendar startTime, Calendar endTime, Pageable pageble);

    @Query(value = "select count(1) from (select count(*) from (select create_time,id,source,operand from d1_operation_log where (CASE WHEN ?1 =''   THEN 1=1 ELSE source like ('%'|| ?1 || '%') END ) and (CASE WHEN ?2 ='' THEN 1=1 ELSE operand like ('%'|| ?2 || '%') END ) and create_time between ?3 and ?4 ) as ope group by ope.source,ope.operand,to_char(ope.create_time, 'YYYY-MM')) a", nativeQuery = true)
    Long countByMonth(String name, String moduleName, Calendar startTime, Calendar endTime);

    @Query(value = "select to_char(ope.create_time, 'YYYY-MM-DD HH24') as date , count(ope.id) as number ,string_agg(distinct ope.source,',')as name , string_agg(distinct ope.operand,',') as module_name from (select create_time,id,source,operand from d1_operation_log where (CASE WHEN ?1 =''  THEN 1=1 ELSE source like ('%'|| ?1 || '%') END ) and (CASE WHEN ?2 ='' THEN 1=1 ELSE operand like ('%'|| ?2 || '%') END ) and create_time between ?3 and ?4 order by create_time desc) as ope group by ope.source,ope.operand,to_char(ope.create_time, 'YYYY-MM-DD HH24') order by ope.source,ope.operand,date desc ", nativeQuery = true)
    List<Map<String,Object>> findByHour(String name, String moduleName, Calendar startTime, Calendar endTime, Pageable pageble);

    @Query(value = "select count(1) from (select count(*) from (select create_time,id,source,operand from d1_operation_log where (CASE WHEN ?1 =''   THEN 1=1 ELSE source like ('%'|| ?1 || '%') END ) and (CASE WHEN ?2 ='' THEN 1=1 ELSE operand like ('%'|| ?2 || '%') END ) and create_time between ?3 and ?4 ) as ope group by ope.source,ope.operand,to_char(ope.create_time, 'YYYY-MM-DD HH')) a", nativeQuery = true)
    Long countByHour(String name, String moduleName, Calendar startTime, Calendar endTime);

    @Query(value = "select to_char(date_trunc('week', ope.create_time),'YYYY-MM-DD') as date , count(ope.id) as number ,string_agg(distinct ope.source,',')as name , string_agg(distinct ope.operand,',') as module_name from (select create_time,id,source,operand from d1_operation_log where (CASE WHEN ?1 =''  THEN 1=1 ELSE source like ('%'|| ?1 || '%') END ) and (CASE WHEN ?2 ='' THEN 1=1 ELSE operand like ('%'|| ?2 || '%') END ) and create_time between ?3 and ?4 order by create_time desc) as ope group by ope.source,ope.operand,to_char(date_trunc('week', ope.create_time),'YYYY-MM-DD') order by ope.source,ope.operand,date desc", nativeQuery = true)
    List<Map<String,Object>> findByWeek(String name, String moduleName, Calendar startTime, Calendar endTime, Pageable pageble);

    @Query(value = "select count(1) from (select count(*) from (select create_time,id,source,operand from d1_operation_log where (CASE WHEN ?1 =''   THEN 1=1 ELSE source like ('%'|| ?1 || '%') END ) and (CASE WHEN ?2 ='' THEN 1=1 ELSE operand like ('%'|| ?2 || '%') END ) and create_time between ?3 and ?4 ) as ope group by ope.source,ope.operand,to_char(date_trunc('week', ope.create_time),'YYYY-MM-DD')) a", nativeQuery = true)
    Long countByWeek(String name, String moduleName, Calendar startTime, Calendar endTime);
}
