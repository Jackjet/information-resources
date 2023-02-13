package d1.project.resource.common.service;

import d1.project.resource.resourcemanage.entity.DataSource;
import d1.project.resource.resourcemanage.entity.DataUnit;
import d1.project.resource.resourcemanage.entity.DataUnitField;

import java.util.List;

public interface IDataSourceService {
    List<DataSource> findAll(String type);
    List<DataUnit> findAllByDataSourceId(String dataSourceId);
    List<DataUnitField> findAllByDataUnitId(String dataUnitId);
}
