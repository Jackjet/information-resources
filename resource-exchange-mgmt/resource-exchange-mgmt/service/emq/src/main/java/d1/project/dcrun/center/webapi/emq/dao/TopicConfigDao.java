package d1.project.dcrun.center.webapi.emq.dao;

import d1.project.dcrun.center.webapi.emq.entity.TopicConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author xuaa
 */
public interface TopicConfigDao extends JpaRepository<TopicConfig, String>, JpaSpecificationExecutor<TopicConfig> {
    /**
     * 查询系统服务下的topic下的配置项
     *
     * @param sysService 系统服务id
     * @param topicId    topicId
     * @return 返回值
     */
    List<TopicConfig> findAllBySysServiceIdAndTopicId(String sysService, String topicId);

    /**
     * 查找配置下的用户
     *
     * @param sysService 系统服务id
     * @param appid      开发者id
     * @param topicId    topicId
     * @return return
     */
    TopicConfig findBySysServiceIdAndAppidAndTopicId(String sysService, String appid, String topicId);

    /**
     * 查找配置
     *
     * @param integrationId 集成项目的id
     * @param sysService    系统服务id
     * @return 返回值
     */
    List<TopicConfig> findAllByIntegrationIdAndSysServiceId(String integrationId, String sysService);

    /**
     * 查找配置
     *
     * @param topicId topicId
     * @return 返回值
     */
    List<TopicConfig> findAllByTopicId(String topicId);

    /**
     * 查询系统服务下的topic下的配置项
     *
     * @param sysService 系统服务id
     * @param topicId    topicId
     * @return 返回值
     */
    List<TopicConfig> findAllBySysServiceIdAndTopicIdAndAppidContains(String sysService, String topicId, String appid);
}
