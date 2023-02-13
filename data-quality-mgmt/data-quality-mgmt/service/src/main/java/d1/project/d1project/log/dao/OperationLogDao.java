package d1.project.d1project.log.dao;

import d1.project.d1project.log.entity.OperationLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-09 09:48
 */
public interface OperationLogDao extends JpaRepository<OperationLogEntity, String>, JpaSpecificationExecutor<OperationLogEntity> {
}
