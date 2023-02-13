package d1.project.tangshan.operation.manage.dao.remote;

import d1.project.tangshan.operation.manage.entity.database.ParameterConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author lin
 */
public interface ParameterConfigDao extends JpaRepository<ParameterConfig, String>, JpaSpecificationExecutor<ParameterConfig> {
    Boolean existsByName(String name);
}
