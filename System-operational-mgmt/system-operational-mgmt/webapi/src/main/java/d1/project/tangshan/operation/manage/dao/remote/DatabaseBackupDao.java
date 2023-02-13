package d1.project.tangshan.operation.manage.dao.remote;

import d1.project.tangshan.operation.manage.entity.database.DatabaseBackup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author lin
 */
public interface DatabaseBackupDao extends JpaRepository<DatabaseBackup, String>, JpaSpecificationExecutor<DatabaseBackup> {
}
