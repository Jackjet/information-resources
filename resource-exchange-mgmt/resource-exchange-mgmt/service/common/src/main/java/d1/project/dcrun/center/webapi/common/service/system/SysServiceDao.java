package d1.project.dcrun.center.webapi.common.service.system;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author xuaa
 */
public interface SysServiceDao extends JpaRepository<SysService, String>, JpaSpecificationExecutor<SysService> {

    /**
     * 系统服务名称是否存在
     *
     * @param name      系统服务名称
     * @param runNodeId 运行节点id
     * @return 返回结果
     */
    Boolean existsByNameAndRunNodeId(String name, String runNodeId);

    /**
     * 根据节点id查询系统服务
     *
     * @param runNodeId 节点id
     * @return 返回结果
     */
    List<SysService> findAllByRunNodeId(String runNodeId);

    @Query(value = "select d.remark as remark,d.template_type as templateType,d.version as version,d.name as name from d1_sys_service d where d.integration_id=?1 and d.template_type=?2 order by d.template_version", nativeQuery = true)
    Page<Map> selectVersion(String integration_id, String templateType, Pageable parameter);

    @Query(value = "select max(to_number(d.template_version,'99999')) from d1_sys_service d where d.integration_id=?1 and d.template_type=?2", nativeQuery = true)
    Integer getVersion(String integration_id, String templateType);

    @Query(value = "select count(id) from d1_sys_service d where d.integration_id=?1 and d.template_type=?2 and d.version=?3", nativeQuery = true)
    Integer getTypeVersionCount(String integrationId, String typeName,String version);

}
