package d1.project.api.integration.apianalysis.view.dao;

import d1.project.api.integration.apianalysis.view.entity.ApiLogRecordList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author baozh
 */
public interface ApiLogRecordListDao extends JpaRepository<ApiLogRecordList, String>, JpaSpecificationExecutor<ApiLogRecordList> {
}
