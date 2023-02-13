package d1.project.tangshan.operation.manage.dao.remote;

import d1.project.tangshan.operation.manage.entity.database.DatabaseMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DatabaseMonitoringDao extends JpaRepository<DatabaseMonitoring, String>, JpaSpecificationExecutor<DatabaseMonitoring> {
    DatabaseMonitoring findFirstByNodeIdAndDatabaseId(String nodeId,String databaseId);
    List<DatabaseMonitoring> findAllByNodeIdAndDatabaseId(String nodeId, String databaseId);
}
