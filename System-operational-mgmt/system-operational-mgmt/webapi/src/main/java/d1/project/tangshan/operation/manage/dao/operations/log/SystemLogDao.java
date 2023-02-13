package d1.project.tangshan.operation.manage.dao.operations.log;

import d1.project.tangshan.operation.manage.entity.operations.log.SystemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author lin
 */
public interface SystemLogDao extends JpaRepository<SystemLog, String>, JpaSpecificationExecutor<SystemLog> {
}
