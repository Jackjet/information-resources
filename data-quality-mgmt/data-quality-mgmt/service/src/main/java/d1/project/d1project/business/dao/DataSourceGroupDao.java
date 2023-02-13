package d1.project.d1project.business.dao;

import d1.project.d1project.business.entity.DataSourceGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author libin
 */
public interface DataSourceGroupDao extends JpaRepository<DataSourceGroup,String>, JpaSpecificationExecutor<DataSourceGroup> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name,String id);

    boolean existsByParentId(String parentId);
}
