package d1.project.tangshan.operation.manage.dao;

import d1.project.tangshan.operation.manage.entity.BaseEnvironmentMonitorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author Buter
 */
public interface BaseEnvironmentMonitorDao extends JpaRepository<BaseEnvironmentMonitorEntity, String>, JpaSpecificationExecutor<BaseEnvironmentMonitorEntity> {

    @Query(value = " SELECT " +
            "     to_char(beme.log_time, 'YYYY-MM') AS month" +
            "     ,COUNT(beme.id) AS count" +
            "     ,dn.id AS nodeId" +
            "     ,beme.status " +
            " FROM " +
            "     d1_base_environment_monitor beme" +
            "     ,d1_node dn" +
            " WHERE " +
            "     beme.intranet_ip = dn.intranet_ip " +
            " AND dn.id = ?1 " +
            " AND to_char(beme.log_time,'yyyy-MM-dd') BETWEEN ?2 AND ?3 " +
            " GROUP BY to_char(beme.log_time, 'YYYY-MM'),dn.id,beme.status", nativeQuery = true)
    List<Map<String, Object>> nodeStatusAnalysis(String nodeId, String startTime, String endTime);
    @Query(value = " SELECT " +
            "    dbem.log_time AS logTime" +
            "    ,dn.name AS nodeName" +
            "    ,dn.id AS nodeId" +
            "    ,dbem.status" +
            "    ,dbem.network" +
            "    ,dbem.db" +
            "    ,dbem.system_software AS systemSoftware" +
            " FROM " +
            "     d1_base_environment_monitor dbem" +
            "    ,d1_node dn " +
            " WHERE " +
            "     dbem.intranet_ip = dn.intranet_ip" +
            " ORDER BY dbem.create_time DESC " +
            " LIMIT 1 ", nativeQuery = true)
    Map<String, Object> firstByIntranetIpOrderByCreateTimeDesc(String intranetIp);
    @Query(value = " SELECT " +
            "    d1_node.name AS nodeName" +
            "    ,d1_node.id AS nodeId" +
            "    ,d1_base_environment_monitor.status" +
            "    ,d1_base_environment_monitor.network" +
            "    ,d1_base_environment_monitor.db" +
            "    ,d1_base_environment_monitor.system_software AS systemSoftware" +
            "    ,d1_base_environment_monitor.log_time AS logTime" +
            "    ,d1_base_environment_monitor.create_time AS createTime" +
            " FROM " +
            "     d1_base_environment_monitor" +
            "     inner join d1_node on d1_base_environment_monitor.intranet_ip = d1_node.intranet_ip" +
            " WHERE " +
            "     (CASE WHEN ?1 ='' THEN 1=1 ELSE d1_node.name like ('%'|| ?1 || '%') END ) " +
            " AND (CASE WHEN ?2 ='' THEN 1=1 ELSE d1_base_environment_monitor.log_time >= TO_TIMESTAMP(?2, 'YYYY-MM-DD HH24:MI:SS') END ) " +
            " AND (CASE WHEN ?3 ='' THEN 1=1 ELSE d1_base_environment_monitor.log_time <= TO_TIMESTAMP(?3, 'YYYY-MM-DD HH24:MI:SS') END ) " +
            " ORDER BY d1_base_environment_monitor.log_time DESC"
            , countQuery = " SELECT " +
            "    count(d1_base_environment_monitor.*) " +
            " FROM " +
            "     d1_base_environment_monitor" +
            "     inner join d1_node on d1_base_environment_monitor.intranet_ip = d1_node.intranet_ip" +
            " WHERE " +
            "     (CASE WHEN ?1 ='' THEN 1=1 ELSE d1_node.name like ('%'|| ?1 || '%') END ) " +
            " AND (CASE WHEN ?2 ='' THEN 1=1 ELSE d1_base_environment_monitor.log_time >= TO_TIMESTAMP(?2, 'YYYY-MM-DD HH24:MI:SS') END ) " +
            " AND (CASE WHEN ?3 ='' THEN 1=1 ELSE d1_base_environment_monitor.log_time <= TO_TIMESTAMP(?3, 'YYYY-MM-DD HH24:MI:SS') END ) "
            , nativeQuery = true)
    Page<Map<String, Object>> findBaseEnvironmentMonitorEntityBy(String nodeId, String startTime, String endTime, Pageable pageable);
}
