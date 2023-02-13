package d1.project.d1project.common.service;

import d1.project.d1project.business.entity.DataUnit;

import java.util.List;

public interface IDataUnitService {
    List<DataUnit> findAllByDataSourceId(String dataSourceId);
}
