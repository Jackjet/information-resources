package d1.project.d1project.business.dao;

import d1.project.d1project.business.entity.TaskLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface TaskLogDao extends JpaRepository<TaskLog, String>, JpaSpecificationExecutor<TaskLog> {

    @Query(value = "SELECT count(id) FROM d1_task_log WHERE execution_time >= ?1 AND execution_time < ?2 ", nativeQuery = true)
    long getTaskCount(LocalDateTime startTime, LocalDateTime endTime);

    @Query(value = "SELECT year_month, count(id) FROM d1_task_log WHERE execution_time >= ?1 AND execution_time < ?2 GROUP BY year_month ORDER BY year_month ASC", nativeQuery = true)
    List<Map<String, Object>> getCountGroupByYearMonth(LocalDateTime startTime, LocalDateTime endTime);

    @Query(value = "SELECT SUM(data_size) AS total_data_size, SUM(error_data_size) AS total_error_data_size FROM d1_task_log WHERE execution_time >= ?1 AND execution_time < ?2", nativeQuery = true)
    Map<String, BigDecimal> getValidateTotalDataCountOrTotalErrorDataSize(LocalDateTime startTime, LocalDateTime endTime);

    @Query(value = "SELECT year_month, SUM(data_size) AS total_data_size, SUM(error_data_size) AS total_error_data_size FROM d1_task_log WHERE execution_time >= ?1 AND execution_time < ?2 GROUP BY year_month ORDER BY year_month ASC", nativeQuery = true)
    List<Map<String, Object>> getValidateTotalDataCountOrTotalErrorDataSizeGroupByYearMonth(LocalDateTime startTime, LocalDateTime endTime);

}
