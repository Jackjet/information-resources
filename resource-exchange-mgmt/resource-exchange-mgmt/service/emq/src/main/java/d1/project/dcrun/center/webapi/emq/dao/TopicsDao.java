package d1.project.dcrun.center.webapi.emq.dao;

import d1.project.dcrun.center.webapi.emq.entity.Topics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author xuaa
 */
public interface TopicsDao extends JpaRepository<Topics, String>, JpaSpecificationExecutor<Topics> {
    /**
     * 同一项目下名称是否重复
     *
     * @param integrationId 集成项目id
     * @param topicName     topic名称
     * @param sysServiceId  sysServiceId
     * @return 返回值
     */
    Boolean existsByIntegrationIdAndTopicNameAndSysServiceId(String integrationId, String topicName, String sysServiceId);
}
