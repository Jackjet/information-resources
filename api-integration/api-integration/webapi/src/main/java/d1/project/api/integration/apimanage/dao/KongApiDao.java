package d1.project.api.integration.apimanage.dao;

import d1.project.api.integration.apimanage.entity.KongApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface KongApiDao extends JpaRepository<KongApi, String>, JpaSpecificationExecutor<KongApi> {
    Boolean existsByApiId(String apiId);
    KongApi findFirstByApiId(String apiId);

    List<KongApi> findAllByApiIdIn(List<String> apiIds);

    KongApi findFirstByRouteId(String routeId);
}
