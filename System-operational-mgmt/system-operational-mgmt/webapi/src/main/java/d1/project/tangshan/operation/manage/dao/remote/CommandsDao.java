package d1.project.tangshan.operation.manage.dao.remote;

import d1.project.tangshan.operation.manage.entity.remote.Commands;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author lin
 */
public interface CommandsDao extends JpaRepository<Commands, String>, JpaSpecificationExecutor<Commands> {
    List<Commands> findAllByContentLike(String content);

}
