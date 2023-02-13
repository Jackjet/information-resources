package d1.project.dcrun.center.webapi.dcapi.dao;

import d1.project.dcrun.center.webapi.dcapi.entity.ApiRouteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author xuaa
 */
public interface ApiRouteUserDao extends JpaRepository<ApiRouteUser, String>, JpaSpecificationExecutor<ApiRouteUser> {
    /**
     * 查询appi数据
     *
     * @param integrationId 集成项目id
     * @param sysServiceId  系统服务id
     * @param appid         开发者id
     * @return 查询结果
     */
    ApiRouteUser findByIntegrationIdAndSysServiceIdAndAppid(String integrationId, String sysServiceId, String appid);

    /**
     * 查询开发者信息
     *
     * @param integrationId 集成项目id
     * @param sysServiceId  系统服务id
     * @return 查询结果
     */
    List<ApiRouteUser> findAllByIntegrationIdAndSysServiceId(String integrationId, String sysServiceId);
}
