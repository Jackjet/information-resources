package d1.project.d1project.business.dao;

import d1.project.d1project.business.entity.DataUnitField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author libin
 */
public interface DataUnitFieldDao extends JpaRepository<DataUnitField,String>, JpaSpecificationExecutor<DataUnitField> {
    void deleteAllByDataUnitId(String dataUnitId);
    void deleteAllByDataSourceId(String dataSourceId);

    List<DataUnitField> findAllByDataUnitId(String dataUnitId);
}
