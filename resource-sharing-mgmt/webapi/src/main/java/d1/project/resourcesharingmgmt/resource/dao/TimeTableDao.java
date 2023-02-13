package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.TimeTableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author maoyuying
 */
public interface TimeTableDao extends JpaRepository<TimeTableEntity, String>, JpaSpecificationExecutor<TimeTableEntity>{
}
