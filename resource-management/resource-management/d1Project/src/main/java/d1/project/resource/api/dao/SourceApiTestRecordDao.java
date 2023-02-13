package d1.project.resource.api.dao;

import d1.project.resource.api.entity.SourceApiTestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author libin
 */
public interface SourceApiTestRecordDao extends JpaRepository<SourceApiTestRecord, String>, JpaSpecificationExecutor<SourceApiTestRecord> {
    List<SourceApiTestRecord> findAllBySourceApiIdOrderByRequestTimeDesc(String sourceApiId);
}
