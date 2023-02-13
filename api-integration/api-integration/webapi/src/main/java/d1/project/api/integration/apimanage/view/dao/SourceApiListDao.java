package d1.project.api.integration.apimanage.view.dao;

import d1.project.api.integration.apimanage.view.entity.SourceApiList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author baozh
 */
public interface SourceApiListDao extends JpaRepository<SourceApiList, String>, JpaSpecificationExecutor<SourceApiList> {
}
