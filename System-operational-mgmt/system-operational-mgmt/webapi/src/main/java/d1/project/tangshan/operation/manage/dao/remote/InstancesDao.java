package d1.project.tangshan.operation.manage.dao.remote;

import d1.project.tangshan.operation.manage.entity.database.Instances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author lin
 */
public interface InstancesDao extends JpaRepository<Instances, String>, JpaSpecificationExecutor<Instances> {
    Boolean existsByName(String name);

    List<Instances> findAllByNodeIdAndDatabaseIdOrderByCreateTimeDesc(String nodeId, String databaseId);
}
