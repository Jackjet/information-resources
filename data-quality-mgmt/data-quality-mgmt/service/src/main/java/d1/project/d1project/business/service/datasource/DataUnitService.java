package d1.project.d1project.business.service.datasource;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenManager;
import d1.project.d1project.business.dao.DataUnitDao;
import d1.project.d1project.business.entity.DataSource;
import d1.project.d1project.business.entity.DataUnit;
import d1.project.d1project.common.service.IDataUnitService;
import d1.project.d1project.common.utils.BaseUtils;
import d1.project.datasource.database.sqlTool.SqlTool;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Vector;

@Service
public class DataUnitService implements IDataUnitService {
    private final DataUnitDao dataUnitDao;

    public DataUnitService(DataUnitDao dataUnitDao) {
        this.dataUnitDao = dataUnitDao;
    }

    public Page<DataUnit> getDataUnit(JSONObject params) throws Exception {
        SpecificationBuilder<DataUnit> builder = new SpecificationBuilder<>();
        Specification<DataUnit> specification = builder.init(params)
                .sEqual("dataSourceId", "dataSourceId")
                .build();
        return dataUnitDao.findAll(specification, builder.getPageable());
    }

    @Transactional(rollbackOn = Exception.class)
    public void updateDataUnit(HttpServletRequest request, DataSource dataSource) throws Exception {
        SqlTool sqlTool = new SqlTool(dataSource.getType(), dataSource.getConnectUrl(), dataSource.getUsername(), dataSource.getPassword());
        List<String> tables = sqlTool.getTableOfSchema(dataSource.getDataBaseName());
        sqlTool.close();

        dataUnitDao.deleteAllByDataSourceId(dataSource.getId());

        List<DataUnit> dataUnits = new Vector<>();
        for (String tableName : tables) {
            DataUnit dataUnit = new DataUnit();
            dataUnit.setId(BaseUtils.generate32Id());
            dataUnit.setName(tableName);
            dataUnit.setDataSourceId(dataSource.getId());
            TokenManager.getInstance().updateCreateIdAndTime(request, dataUnit);

            dataUnits.add(dataUnit);
        }

        dataUnitDao.saveAll(dataUnits);
    }

    public DataUnit getDataUnitById(String id) throws Exception {
        return dataUnitDao.findById(id).orElseThrow(() -> new DoValidException("信息不存在"));
    }

    @Override
    public List<DataUnit> findAllByDataSourceId(String dataSourceId) {
        return dataUnitDao.findAllByDataSourceId(dataSourceId);
    }
}
