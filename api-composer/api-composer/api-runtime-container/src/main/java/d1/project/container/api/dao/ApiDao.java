package d1.project.container.api.dao;

import d1.project.container.api.entity.ApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ApiDao extends JpaRepository<ApiEntity, String>, JpaSpecificationExecutor<ApiEntity> {

    Optional<ApiEntity> findFirstByPathAndMethod(String path, String method);

}
