package d1.project.d1project.common.service;

import d1.project.d1project.business.entity.DataSource;

import java.util.List;

public interface IDataSourceService {
    List<DataSource> findAllEnable();
}
