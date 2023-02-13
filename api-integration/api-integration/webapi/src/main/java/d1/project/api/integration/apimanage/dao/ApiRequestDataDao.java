package d1.project.api.integration.apimanage.dao;

import d1.project.api.integration.apimanage.entity.ApiRequestData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author baozh
 */
public interface ApiRequestDataDao extends JpaRepository<ApiRequestData, String>, JpaSpecificationExecutor<ApiRequestData> {
}
