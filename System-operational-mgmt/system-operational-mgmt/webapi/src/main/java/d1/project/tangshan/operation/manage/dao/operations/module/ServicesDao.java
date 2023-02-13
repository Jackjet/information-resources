package d1.project.tangshan.operation.manage.dao.operations.module;

import d1.project.tangshan.operation.manage.entity.operations.module.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author lin
 */
public interface ServicesDao extends JpaRepository<Services, String>, JpaSpecificationExecutor<Services> {
    Boolean existsByName(String name);

    Boolean existsByNodeIdAndPort(String nodeId, String port);

    List<Services> findAllByNodeIdOrderByCreateTimeDesc(String nodeId);
}
