package d1.project.dcrun.center.webapi.datacache.dao;

import d1.project.dcrun.center.webapi.datacache.entity.KeyConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


/**
 * @author maoyy
 */
public interface KeyConfigDao extends JpaRepository<KeyConfig, String>, JpaSpecificationExecutor<KeyConfig> {
    /**
     * 根据key查询，所有key权限信息
     *
     * @param sysServiceId
     * @param key
     * @return
     */
    List<KeyConfig> findAllBySysServiceIdAndKey(String sysServiceId, String key);

    /**
     * 查询开发者是否存在key泉下
     *
     * @param sysServiceId
     * @param appid
     * @param key
     * @return
     */
    KeyConfig findBySysServiceIdAndAppidAndKey(String sysServiceId, String appid, String key);

    /**
     * @param integrationId
     * @param sysServiceId
     * @return
     */
    List<KeyConfig> findAllByIntegrationIdAndSysServiceId(String integrationId, String sysServiceId);

}
