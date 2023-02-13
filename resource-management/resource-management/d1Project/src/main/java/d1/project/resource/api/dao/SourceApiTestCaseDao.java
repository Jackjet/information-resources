package d1.project.resource.api.dao;

import d1.project.resource.api.entity.SourceApiTestCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SourceApiTestCaseDao extends JpaRepository<SourceApiTestCase, String>, JpaSpecificationExecutor<SourceApiTestCase> {
    List<SourceApiTestCase> findAllBySourceApiIdOrderByCreateTimeDesc(String sourceApiId);
}
