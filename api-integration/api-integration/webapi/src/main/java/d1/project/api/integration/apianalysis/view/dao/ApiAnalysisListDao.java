package d1.project.api.integration.apianalysis.view.dao;

import d1.project.api.integration.apianalysis.view.entity.ApiAnalysisList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author baozh
 */
public interface ApiAnalysisListDao extends JpaRepository<ApiAnalysisList, String>, JpaSpecificationExecutor<ApiAnalysisList> {
}
