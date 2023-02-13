package d1.project.resource.resourcemanage.dao;

import d1.project.resource.resourcemanage.entity.DataSource;
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

    List<DataSource> findAllByStatus(String status);

    List<DataSource> findAllByTypeAndStatus(String type, String status);
}
