package d1.project.api.integration.application.dao;

import d1.project.api.integration.application.entity.NormalConsumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author libin
 */
public interface NormalConsumerDao extends JpaRepository<NormalConsumer,String>, JpaSpecificationExecutor<NormalConsumer> {
    NormalConsumer findFirstByAppid(String appid);
    List<NormalConsumer> findAllByAppidIn(List<String> appids);

    NormalConsumer findFirstByConsumerId(String consumerId);
}
