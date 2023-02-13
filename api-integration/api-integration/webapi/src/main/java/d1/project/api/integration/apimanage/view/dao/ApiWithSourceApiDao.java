package d1.project.api.integration.apimanage.view.dao;

import d1.project.api.integration.apimanage.view.entity.ApiWithSourceApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * @author baozh
 */
public interface ApiWithSourceApiDao extends JpaRepository<ApiWithSourceApi, String>, JpaSpecificationExecutor<ApiWithSourceApi> {
}
