package d1.project.api.integration.apimanage.dao;

import d1.project.api.integration.apimanage.entity.ApiAuthManage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author baozh
 */
public interface ApiAuthManageDao extends JpaRepository<ApiAuthManage, String>, JpaSpecificationExecutor<ApiAuthManage> {
    /**
     * 根据API ID 查询是否存在数据
     *
     * @param apiId API ID
     * @return 查询结果
     */
    boolean existsAllByApiId(String apiId);

    /**
     * 根据ID批量删除
     *
     * @param ids 数据ID
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteAllByIdIn(List<String> ids);

    /**
     * 根据ID批量删除
     *
     * @param appIds 数据ID
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteAllByAppIdIn(List<String> appIds);

    /**
     * 根据APIid批量删除授权
     * @param apiId
     */
    void deleteAllByApiId(String apiId);

    /**
     * 应用ID查询
     *
     * @param appIds appIds
     * @return 时候存在
     */
    boolean existsAllByAppIdIn(List<String> appIds);

    /**
     * 应用ID查询
     *
     * @param appId appId
     * @return 时候存在
     */
    boolean existsAllByAppId(String appId);

    /**
     * 根据apiId和应用ID查询
     *
     * @param appId appId
     * @param apiId apiId
     * @return 授权信息
     */
    ApiAuthManage findFirstByAppIdAndApiId(String appId, String apiId);

    /**
     * 根据apiId和appId查询
     * @param apiId ApiId
     * @param appId 应用Id
     * @return
     */
    ApiAuthManage findFirstByApiIdAndAppId(String apiId, String appId);

    /**
     * 根据AppId删除授权
     * @param appId
     */
    void deleteAllByAppId(String appId);
}
