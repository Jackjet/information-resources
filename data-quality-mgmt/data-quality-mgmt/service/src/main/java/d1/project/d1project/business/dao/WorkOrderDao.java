package d1.project.d1project.business.dao;

import d1.project.d1project.business.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface WorkOrderDao extends JpaRepository<WorkOrder, String>, JpaSpecificationExecutor<WorkOrder> {

    long countByHandlerId(String handlerId);

    long countByHandlerIdAndStatus(String handlerId, int status);

    long countByHandlerIdAndStatusNot(String handlerId, int status);

    Optional<WorkOrder> findFirstByTitle(String title);

    @Query(value = "SELECT count(id) FROM d1_work_order WHERE create_time >= ?1 AND create_time < ?2", nativeQuery = true)
    long getCreateWorkOrderCount(LocalDateTime startTime, LocalDateTime endTime);

    @Query(value = "SELECT count(id) FROM d1_work_order WHERE create_time >= ?1 AND create_time < ?2 AND status <> 0", nativeQuery = true)
    long getCloseWorkOrderCount(LocalDateTime startTime, LocalDateTime endTime);

    @Query(value = "SELECT level, count(id)  FROM d1_work_order WHERE create_time >= ?1 AND create_time < ?2 GROUP BY level", nativeQuery = true)
    List<Map<Integer, Long>> getCountGroupByLevel(LocalDateTime startTime, LocalDateTime endTime);

    @Query(value = "SELECT create_by_name, count(id)  FROM d1_work_order WHERE create_time >= ?1 AND create_time < ?2 GROUP BY create_by_name", nativeQuery = true)
    List<Map<Integer, Long>> getCountGroupByCreateByName(LocalDateTime startTime, LocalDateTime endTime);

    @Query(value = "SELECT handler_name, count(id)  FROM d1_work_order WHERE create_time >= ?1 AND create_time < ?2 GROUP BY handler_name", nativeQuery = true)
    List<Map<Integer, Long>> getCountGroupByHandlerName(LocalDateTime startTime, LocalDateTime endTime);

}
