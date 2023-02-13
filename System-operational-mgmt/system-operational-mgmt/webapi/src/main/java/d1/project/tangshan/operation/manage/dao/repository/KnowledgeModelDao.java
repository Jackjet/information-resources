package d1.project.tangshan.operation.manage.dao.repository;

import d1.project.tangshan.operation.manage.entity.repository.KnowledgeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author lin
 */
public interface KnowledgeModelDao extends JpaRepository<KnowledgeModel, String>, JpaSpecificationExecutor<KnowledgeModel> {
    Boolean existsByName(String name);
}
