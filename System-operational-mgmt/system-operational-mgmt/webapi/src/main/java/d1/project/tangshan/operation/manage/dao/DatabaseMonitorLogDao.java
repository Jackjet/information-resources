package d1.project.tangshan.operation.manage.dao;

import d1.project.tangshan.operation.manage.entity.database.DatabaseMonitorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DatabaseMonitorLogDao extends JpaRepository<DatabaseMonitorLog, String>, JpaSpecificationExecutor<DatabaseMonitorLog> {
    DatabaseMonitorLog findFirstByNodeIdAndDatabaseIdOrderByCreateTimeDesc(String nodeId, String databaseId);
}
