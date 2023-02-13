package d1.project.resource.resourcemanage.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.entity.BaseEntity;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenManager;
import d1.project.resource.common.service.IDataSourceService;
import d1.project.resource.common.utils.BaseUtils;
import d1.project.resource.resourcemanage.dao.DataSourceDao;
import d1.project.resource.resourcemanage.dao.DataUnitDao;
import d1.project.resource.resourcemanage.dao.DataUnitFieldDao;
import d1.project.resource.resourcemanage.entity.DataSource;
import d1.project.resource.resourcemanage.entity.DataUnit;
import d1.project.resource.resourcemanage.entity.DataUnitField;
import d1.project.resource.resourcemanage.model.DataSourceManageDataSourceInsertPostVm;
import d1.project.resource.resourcemanage.model.DataSourceManageDataSourceUpdatePutVm;
import d1.project.resource.resourcemanage.utils.Constants;
import datasource.constants.DatabaseConstants;
import datasource.database.sqlTool.SqlTool;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;

/**
 * @author libin
 */
@Service
public class DataSourceService implements IDataSourceService {
    private final DataSourceDao dataSourceDao;
    private final DataUnitDao dataUnitDao;
    private final DataUnitFieldDao dataUnitFieldDao;

    public DataSourceService(DataSourceDao dataSourceDao, DataUnitDao dataUnitDao, DataUnitFieldDao dataUnitFieldDao) {
        this.dataSourceDao = dataSourceDao;
        this.dataUnitDao = dataUnitDao;
        this.dataUnitFieldDao = dataUnitFieldDao;
    }

    public void insert(HttpServletRequest request, DataSourceManageDataSourceInsertPostVm params) throws DoValidException {
        if (dataSourceDao.existsByName(params.getName())) {
            throw new DoValidException("已存在同名数据源");
        }

        DataSource dataSource = new DataSource();
        BeanUtils.copyProperties(params, dataSource);

        dataSource.setConnectUrl(generateUrl(params.getType(), params.getIp(), params.getPort(), params.getDataBaseName(), params.getServiceName()));

        dataSource.setId(BaseUtils.generate32Id());
        dataSource.setStatus(Constants.DATASOURCE_STATUS_ENABLE);

        JSONObject user = BaseUtils.getUserInfo(request);
        dataSource.setCreateById(user.getString("id"));
        dataSource.setCreateTime(Calendar.getInstance());

        dataSourceDao.save(dataSource);
    }

    public void update(DataSourceManageDataSourceUpdatePutVm params) throws DoValidException {
        if (dataSourceDao.existsByNameAndIdNot(params.getName(), params.getId())) {
            throw new DoValidException("已存在同名数据源");
        }

        DataSource data = dataSourceDao.findById(params.getId()).orElseThrow(() -> new DoValidException("数据源信息不存在"));
        BeanUtils.copyProperties(params, data);
        data.setConnectUrl(generateUrl(params.getType(), params.getIp(), params.getPort(), params.getDataBaseName(), params.getServiceName()));
        dataSourceDao.save(data);
    }

    public void dataSourceEnableOrForbidden(String id) throws DoValidException {
        DataSource data = dataSourceDao.findById(id).orElseThrow(() -> new DoValidException("数据源信息不存在"));
        if (Constants.DATASOURCE_STATUS_ENABLE.equals(data.getStatus())) {
            data.setStatus(Constants.DATASOURCE_STATUS_UNENABLE);
        } else {
            data.setStatus(Constants.DATASOURCE_STATUS_ENABLE);
        }

        dataSourceDao.save(data);
    }

    public Page<DataSource> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<DataSource> builder = new SpecificationBuilder<>();
        Specification<DataSource> specification = builder.init(params)
                .sContain("name", "name")
                .sEqual("type", "type")
                .sEqual("groupId", "groupId")
                .sEqual("status", "status")
                .dOrder("createTime")
                .build();
        return dataSourceDao.findAll(specification, builder.getPageable());
    }

    public DataSource findById(String id) throws Exception {
        return dataSourceDao.findById(id).orElseThrow(() -> new DoValidException("数据源信息不存在"));
    }

    @Transactional(rollbackOn = Exception.class)
    public void deleteById(String id) throws DoValidException {
        if (dataSourceDao.existsByIdAndStatus(id, Constants.DATASOURCE_STATUS_ENABLE)) {
            throw new DoValidException("数据源已启用，禁止删除");
        }
        dataUnitFieldDao.deleteAllByDataSourceId(id);
        dataUnitDao.deleteAllByDataSourceId(id);
        dataSourceDao.deleteById(id);
    }

    public boolean connectTest(String type, String ip, String port, String databaseName, String serviceName, String username, String password) {
        try {
            SqlTool sqlTool = new SqlTool(type, generateUrl(type, ip, port, databaseName, serviceName), username, password);
            boolean result = sqlTool.connectTest();
            sqlTool.close();

            return result;
        } catch (Exception e) {
            return false;
        }
    }

    public List<DataSource> findAllEnable() {
        return dataSourceDao.findAllByStatusOrderByCreateTime(Constants.DATASOURCE_STATUS_ENABLE);
    }

    //==============================================================
    private String generateUrl(String type, String ip, String port, String databasename, String serviceName) throws DoValidException {
        String urlFormat = "";
        String url = "";
        switch (type) {
            case DatabaseConstants.MYSQL:
                urlFormat = "jdbc:mysql://%s:%s/%s";
                url = String.format(urlFormat, ip, port, databasename);
                break;
            case DatabaseConstants.ORACLE:
                urlFormat = "jdbc:oracle:thin:@//%s:%s/%s";
                url = String.format(urlFormat, ip, port, serviceName);
                break;
            case DatabaseConstants.POSTGRESQL:
                urlFormat = "jdbc:postgresql://%s:%s/%s";
                url = String.format(urlFormat, ip, port, databasename);
                break;
            case DatabaseConstants.SQL_SERVER:
                urlFormat = "jdbc:sqlserver://%s:%s;DatabaseName=%s";
                url = String.format(urlFormat, ip, port, databasename);
                break;
            default:
                throw new DoValidException("非法数据库类型");
        }

        return url;
    }

    //================================================================================
    @Override
    public List<DataSource> findAll(String type) {
        if(StringUtils.isEmpty(type)){
            return dataSourceDao.findAllByStatus("1");
        }else {
            return dataSourceDao.findAllByTypeAndStatus(type, "1");
        }
    }

    @Override
    public List<DataUnit> findAllByDataSourceId(String dataSourceId) {
        return dataUnitDao.findAllByDataSourceId(dataSourceId);
    }

    @Override
    public List<DataUnitField> findAllByDataUnitId(String dataUnitId) {
        return dataUnitFieldDao.findAllByDataUnitId(dataUnitId);
    }
}
