package d1.project.dcrun.center.webapi.datacache.dao;

import d1.project.dcrun.center.webapi.datacache.entity.Keys;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author maoyys
 */
public interface KeysDao extends JpaRepository<Keys, String>, JpaSpecificationExecutor<Keys> {
    /**
     * 查询key
     *
     * @param integrationId
     * @param sysServiceId
     * @param key
     * @return
     */
    Keys findByIntegrationIdAndSysServiceIdAndKey(String integrationId, String sysServiceId, String key);

}
