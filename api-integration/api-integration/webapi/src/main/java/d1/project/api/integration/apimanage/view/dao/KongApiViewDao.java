package d1.project.api.integration.apimanage.view.dao;

import d1.project.api.integration.apimanage.view.entity.KongApiView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface KongApiViewDao extends JpaRepository<KongApiView,String>, JpaSpecificationExecutor<KongApiView> {
    List<KongApiView> findAllByApiIdIn(List<String> apiIds);
}
