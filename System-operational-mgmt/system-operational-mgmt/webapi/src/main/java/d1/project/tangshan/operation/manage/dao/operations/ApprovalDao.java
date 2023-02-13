package d1.project.tangshan.operation.manage.dao.operations;

import d1.project.tangshan.operation.manage.entity.operations.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author lin
 */
public interface ApprovalDao extends JpaRepository<Approval, String>, JpaSpecificationExecutor<Approval> {
}
