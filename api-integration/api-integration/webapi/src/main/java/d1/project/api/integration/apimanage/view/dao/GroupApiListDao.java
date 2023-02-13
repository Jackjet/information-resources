package d1.project.api.integration.apimanage.view.dao;

import d1.project.api.integration.apimanage.view.entity.GroupApiList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author baozh
 */
public interface GroupApiListDao extends JpaRepository<GroupApiList, String>, JpaSpecificationExecutor<GroupApiList> {
}
