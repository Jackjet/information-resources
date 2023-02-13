package d1.project.api.integration.apimanage.dao;

import d1.project.api.integration.apimanage.entity.Meta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author libin
 */
public interface MetaDao extends JpaRepository<Meta, String>, JpaSpecificationExecutor<Meta> {
    List<Meta> findAllByAssociationsId(String associationsId);
}
