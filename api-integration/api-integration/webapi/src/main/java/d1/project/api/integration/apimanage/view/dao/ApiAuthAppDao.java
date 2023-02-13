package d1.project.api.integration.apimanage.view.dao;

import d1.project.api.integration.apimanage.view.entity.ApiAuthApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author baozh
 */
public interface ApiAuthAppDao extends JpaRepository<ApiAuthApp, String>, JpaSpecificationExecutor<ApiAuthApp> {
}
