package d1.project.resource.api.dao;

import d1.project.resource.api.model.SourceApiView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author baozh
 */
public interface SourceApiViewDao extends JpaRepository<SourceApiView, String>, JpaSpecificationExecutor<SourceApiView> {

}
