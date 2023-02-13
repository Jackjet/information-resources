package d1.project.tangshan.operation.manage.dao;

import d1.project.tangshan.operation.manage.entity.BigDataApiPlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Buter
 */
public interface BigDataApiPlanDao extends JpaRepository<BigDataApiPlanEntity, String>, JpaSpecificationExecutor<BigDataApiPlanEntity> {
    Boolean existsByName(String name);
}
