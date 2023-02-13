package d1.project.d1project.common.service;

import d1.project.d1project.business.entity.DataUnitField;

import java.util.List;

public interface IDataUnitFieldService {
    List<DataUnitField> findAllByDataUnitId(String dataUnitId);
}
