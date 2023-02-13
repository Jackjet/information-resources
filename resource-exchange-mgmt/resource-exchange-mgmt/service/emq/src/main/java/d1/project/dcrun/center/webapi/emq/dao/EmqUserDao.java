package d1.project.dcrun.center.webapi.emq.dao;

import d1.project.dcrun.center.webapi.emq.entity.EmqUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author xuaa
 */
public interface EmqUserDao extends JpaRepository<EmqUser, String>, JpaSpecificationExecutor<EmqUser> {
    /**
     * 查询该项目写的所有开发者
     *
     * @param integrationId 集成项目id
     * @param sysServiceId  系统服务id
     * @return return
     */
    List<EmqUser> findAllByIntegrationIdAndSysServiceId(String integrationId, String sysServiceId);

    /**
     * 查询改项目下的appid数据
     *
     * @param integrationId 集成项目id
     * @param sysServiceId  系统服务id
     * @param appid         开发者id
     * @return return
     */
    EmqUser findByIntegrationIdAndAppidAndSysServiceId(String integrationId, String appid, String sysServiceId);

}
