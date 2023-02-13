package d1.framework.permission.dao;


import d1.framework.permission.entity.OperationLog1;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author lin
 */
public interface OperationLogDao1 extends JpaRepository<OperationLog1, String>, JpaSpecificationExecutor<OperationLog1> {

}
