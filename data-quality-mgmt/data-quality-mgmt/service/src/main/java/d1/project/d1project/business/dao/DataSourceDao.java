package d1.project.d1project.business.dao;

import d1.project.d1project.business.entity.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author libin
 */
public interface DataSourceDao extends JpaRepository<DataSource,String>, JpaSpecificationExecutor<DataSource> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name,String id);
    boolean existsByGroupId(String groupId);
    boolean existsByIdAndStatus(String id,String status);

    List<DataSource> findAllByStatusOrderByCreateTime(String status);
}
