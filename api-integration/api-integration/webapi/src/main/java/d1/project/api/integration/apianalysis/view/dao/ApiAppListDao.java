package d1.project.api.integration.apianalysis.view.dao;

import d1.project.api.integration.apianalysis.view.entity.ApiAppList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author baozh
 */
public interface ApiAppListDao extends JpaRepository<ApiAppList, String>, JpaSpecificationExecutor<ApiAppList> {
}
