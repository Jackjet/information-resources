package d1.project.api.integration.kong.dao;

import d1.project.api.integration.kong.entity.DefaultConsumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author libin
 */
public interface DefaultConsumerDao extends JpaRepository<DefaultConsumer,String>, JpaSpecificationExecutor<DefaultConsumer> {
    DefaultConsumer findFirstByContainer(String container);
}
