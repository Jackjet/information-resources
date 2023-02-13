package d1.project.resource.resourcemanage.dao;

import d1.project.resource.resourcemanage.entity.DataUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author libin
 */
public interface DataUnitDao extends JpaRepository<DataUnit,String>, JpaSpecificationExecutor<DataUnit> {
    List<DataUnit> findAllByDataSourceId(String dataSourceId);
    void deleteAllByDataSourceId(String dataSourceId);
}
