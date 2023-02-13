package d1.project.api.integration.apimanage.dao;

import d1.project.api.integration.apimanage.entity.KongSourceApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface KongSourceApiDao extends JpaRepository<KongSourceApi, String>, JpaSpecificationExecutor<KongSourceApi> {
    KongSourceApi findFirstBySourceId(String sourceId);
}
