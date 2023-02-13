package d1.project.api.integration.apimanage.view.dao;

import d1.project.api.integration.apimanage.view.entity.AuthAppList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author baozh
 */

public interface AuthAppListDao extends JpaRepository<AuthAppList, String>, JpaSpecificationExecutor<AuthAppList> {
}
