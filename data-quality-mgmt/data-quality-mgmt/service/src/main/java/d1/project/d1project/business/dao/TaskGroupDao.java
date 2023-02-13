package d1.project.d1project.business.dao;

import d1.project.d1project.business.entity.TaskGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TaskGroupDao extends JpaRepository<TaskGroup,String>, JpaSpecificationExecutor<TaskGroup> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name,String id);
    boolean existsByParentId(String parenId);
}
