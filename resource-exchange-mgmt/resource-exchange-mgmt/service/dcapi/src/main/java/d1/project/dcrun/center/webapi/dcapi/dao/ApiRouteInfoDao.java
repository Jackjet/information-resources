package d1.project.dcrun.center.webapi.dcapi.dao;

import d1.project.dcrun.center.webapi.dcapi.entity.ApiRouteInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author lin
 */
public interface ApiRouteInfoDao extends JpaRepository<ApiRouteInfo, String>, JpaSpecificationExecutor<ApiRouteInfo> {
    /**
     * 判断api名称是否重复
     *
     * @param integrationId 集成项目id
     * @param sysServiceId  系统服务id
     * @param name          路由名称
     * @return 返回结果
     */
    Boolean existsByIntegrationIdAndSysServiceIdAndName(String integrationId, String sysServiceId, String name);

    /**
     * 同步路由数据 根据集成项目id查询列表
     *
     * @param integrationId 集成项目id
     * @return 返回结果
     */
    @Query(value = "SELECT api.id,api.name,api.source_method,api.source_path,api.target_method,api.target_path," +
            "api.access_strategy_id,access.type as access_type,access.operation,access.ips,access.app_ids," +
            "api.flow_strategy_id,flow.type as flow_type ,flow.time,flow.api_flow_limit,flow.app_id_flow_limit,flow.ip_flow_limit " +
            "FROM d1_api_route_info api LEFT JOIN d1_access_control_plan access  " +
            "on api.access_strategy_id = access.id LEFT JOIN d1_flow_control_plan flow  " +
            "on flow.id = api.flow_strategy_id where api.integration_id=?1 and access.integration_id=?1 and flow.integration_id=?1 " +
            "and api.sys_service_id = ?2 and access.sys_service_id = ?2", nativeQuery = true)
    List<Map<String, Object>> findAllByIntegrationIdAndSysServiceId(String integrationId, String sysServiceId);
}
