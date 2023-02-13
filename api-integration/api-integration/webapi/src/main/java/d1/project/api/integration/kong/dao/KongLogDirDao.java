package d1.project.api.integration.kong.dao;

import d1.project.api.integration.kong.entity.KongLogDir;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface KongLogDirDao extends JpaRepository<KongLogDir,String>, JpaSpecificationExecutor<KongLogDir> {
    List<KongLogDir> findAllByContainer(String container);
}
