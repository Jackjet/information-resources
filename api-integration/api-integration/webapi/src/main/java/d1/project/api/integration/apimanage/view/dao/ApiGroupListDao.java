package d1.project.api.integration.apimanage.view.dao;

import d1.project.api.integration.apimanage.view.entity.ApiGroupList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author baozh
 */
public interface ApiGroupListDao extends JpaRepository<ApiGroupList, String>, JpaSpecificationExecutor<ApiGroupList> {
}
