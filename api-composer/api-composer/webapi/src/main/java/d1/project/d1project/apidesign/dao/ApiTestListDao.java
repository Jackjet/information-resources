package d1.project.d1project.apidesign.dao;

import d1.project.d1project.apidesign.entity.ApiTestList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author baozh
 */
public interface ApiTestListDao extends JpaRepository<ApiTestList, String>, JpaSpecificationExecutor<ApiTestList> {
}
