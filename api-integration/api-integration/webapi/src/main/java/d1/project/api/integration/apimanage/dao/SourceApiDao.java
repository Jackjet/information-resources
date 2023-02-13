package d1.project.api.integration.apimanage.dao;

import d1.project.api.integration.apimanage.entity.SourceApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface SourceApiDao extends JpaRepository<SourceApi, String>, JpaSpecificationExecutor<SourceApi> {
    SourceApi findFirstByApiId(String apiId);

    List<SourceApi> findAllByApiId(String apiId);
    List<SourceApi> findAllByApiIdIn(List<String> apiIds);
}
