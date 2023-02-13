package d1.project.tangshan.operation.manage.dao;

import d1.project.tangshan.operation.manage.entity.ResourceMonitorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author Buter
 */
public interface ResourceMonitorDao extends JpaRepository<ResourceMonitorEntity, String>, JpaSpecificationExecutor<ResourceMonitorEntity> {

    @Query(value = "select * from (" +
            " select cpu,ram,rom,log_time,to_char( log_time, 'YYYY-MM-DD' ) as date ,ROW_NUMBER() over(partition by to_char( log_time, 'YYYY-MM-DD' ) order by log_time desc) as new_index " +
            " from d1_resource_monitor " +
            " where " +
            "     intranet_ip = ?1" +
            " and log_time >= ?2" +
            " and log_time <= ?3" +
            " ) a" +
            " where a.new_index=1 order by log_time desc"
            , nativeQuery = true)
    List<Map<String,Object>> findByDay(String intranetIp, Calendar startTime, Calendar endTime);

    @Query(value = " SELECT " +
            "    drm.cpu" +
            "    ,dn.name AS nodeName" +
            "    ,drm.ram" +
            "    ,drm.rom" +
            "    ,to_char( drm.log_time, 'YYYY-MM-DD' ) as date" +
            " FROM " +
            "     d1_resource_monitor drm" +
            "    ,d1_node dn " +
            " WHERE " +
            "     drm.intranet_ip = ?1" +
            " AND drm.intranet_ip = dn.intranet_ip" +
            " ORDER BY drm.create_time DESC " +
            " LIMIT 1 ", nativeQuery = true)
    Map<String, Object> findFirstByIntranetIpOrderByLogTimeDesc(String intranetIp);
}
