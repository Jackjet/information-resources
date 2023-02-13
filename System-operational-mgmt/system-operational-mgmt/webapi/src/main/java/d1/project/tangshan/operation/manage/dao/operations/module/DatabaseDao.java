package d1.project.tangshan.operation.manage.dao.operations.module;

import d1.project.tangshan.operation.manage.entity.operations.module.Database;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author lin
 */
public interface DatabaseDao extends JpaRepository<Database, String>, JpaSpecificationExecutor<Database> {
    Boolean existsByName(String name);

    Boolean existsByNodeIdAndPort(String nodeId, String port);

    List<Database> findAllByNodeIdOrderByCreateTimeDesc(String nodeId);
    List<Database> findAllByNodeId(String nodeId);
}
