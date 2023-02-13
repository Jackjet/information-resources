package d1.project.resource.resourcemanage.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.resource.common.utils.BaseUtils;
import d1.project.resource.resourcemanage.dao.DataUnitFieldDao;
import d1.project.resource.resourcemanage.entity.DataSource;
import d1.project.resource.resourcemanage.entity.DataUnit;
import d1.project.resource.resourcemanage.entity.DataUnitField;
import d1.project.resource.resourcemanage.model.field.DataUnitFieldUpdateDataUnitFieldsReplacePutVm;
import datasource.database.sqlTool.SqlTool;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Vector;

/**
 * @author libin
 */
@Service
public class DataUnitFieldService {
    private final DataUnitFieldDao dataUnitFieldDao;

    public DataUnitFieldService(DataUnitFieldDao dataUnitFieldDao) {
        this.dataUnitFieldDao = dataUnitFieldDao;
    }

    public Page<DataUnitField> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<DataUnitField> builder = new SpecificationBuilder<>();
        Specification<DataUnitField> specification = builder.init(params)
                .sEqual("dataUnitId", "dataUnitId")
                .build();
        return dataUnitFieldDao.findAll(specification, builder.getPageable());
    }

    @Transactional(rollbackOn = Exception.class)
    public void update(DataSource dataSource, DataUnit dataUnit) throws Exception {
        SqlTool sqlTool = new SqlTool(dataSource.getType(), dataSource.getConnectUrl(), dataSource.getUsername(), dataSource.getPassword());
        JSONArray columns = sqlTool.getColumns(dataUnit.getName());
        sqlTool.close();

        dataUnitFieldDao.deleteAllByDataUnitId(dataUnit.getId());

        List<DataUnitField> dataUnitFields = new Vector<>();
        for (Object obj : columns) {
            JSONObject column = (JSONObject) obj;
            DataUnitField dataUnitField = column.toJavaObject(DataUnitField.class);
            dataUnitField.setId(BaseUtils.generate32Id());
            dataUnitField.setDataUnitId(dataUnit.getId());
            dataUnitField.setDataSourceId(dataSource.getId());
            dataUnitFields.add(dataUnitField);
        }

        dataUnitFieldDao.saveAll(dataUnitFields);
    }

    public void deleteBySourceId(String dataSourceId) {
        dataUnitFieldDao.deleteAllByDataSourceId(dataSourceId);
    }

    public List<DataUnitField> findAllByDataUnitId(String dataUnitId) {
        return dataUnitFieldDao.findAllByDataUnitId(dataUnitId);
    }

    public void updateFieldReplace(DataUnitFieldUpdateDataUnitFieldsReplacePutVm params) throws DoValidException {
        DataUnitField dataUnitField = dataUnitFieldDao.findById(params.getId()).orElseThrow(() -> new DoValidException("字段信息不存在"));

        dataUnitField.setHasReplace(params.getHasReplace());
        if (params.getHasReplace() == 0) {
            dataUnitField.setRuleName("");
            dataUnitField.setReplaceSql("");
        } else {
            dataUnitField.setRuleName(params.getRuleName());
            dataUnitField.setReplaceSql(params.getReplaceSql());
        }

        dataUnitFieldDao.save(dataUnitField);
    }
}
