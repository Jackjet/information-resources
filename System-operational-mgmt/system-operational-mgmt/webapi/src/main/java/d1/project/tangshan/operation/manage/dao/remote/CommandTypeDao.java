package d1.project.tangshan.operation.manage.dao.remote;

import d1.project.tangshan.operation.manage.entity.remote.CommandType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author lin
 */
public interface CommandTypeDao extends JpaRepository<CommandType, String>, JpaSpecificationExecutor<CommandType> {
    Boolean existsByName(String name);
}
