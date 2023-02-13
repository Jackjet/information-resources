package d1.project.dcrun.center.webapi.common.service.operation.record;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author maoyy
 */
public interface OperationRecordDao extends JpaRepository<OperationRecord, String>, JpaSpecificationExecutor<OperationRecord> {

}
