package d1.project.api.integration.apianalysis.view.dao;

import d1.project.api.integration.apianalysis.view.entity.AppAnalysisList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author baozh
 */
public interface AppAnalysisListDao extends JpaRepository<AppAnalysisList, String>, JpaSpecificationExecutor<AppAnalysisList> {
}
