package d1.project.dcrun.center.webapi.dcapi.dao;

import d1.project.dcrun.center.webapi.dcapi.entity.view.ApiView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author lin
 */
public interface ApiViewDao extends JpaRepository<ApiView, String>, JpaSpecificationExecutor<ApiView> {
}
