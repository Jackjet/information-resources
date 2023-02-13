package d1.project.api.integration.apianalysis.view.dao;

import d1.project.api.integration.apianalysis.view.entity.AppApiList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author baozh
 */
public interface AppApiListDao extends JpaRepository<AppApiList, String>, JpaSpecificationExecutor<AppApiList> {
}
