package d1.project.d1project.business.dao;

import d1.project.d1project.business.entity.VerifyRuleLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VerifyRuleLogDao extends JpaRepository<VerifyRuleLog, String>, JpaSpecificationExecutor<VerifyRuleLog> {

}
