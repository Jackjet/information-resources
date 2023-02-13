package d1.project.tangshan.operation.manage.service.database;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.remote.DatabaseMonitoringDao;
import d1.project.tangshan.operation.manage.entity.database.DatabaseMonitoring;
import d1.project.tangshan.operation.manage.service.operations.log.OperationLogService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lin
 */
@Service
public class DatabaseMonitoringService {
    private final DatabaseMonitoringDao databaseMonitoringDao;
    private final OperationLogService operationLogService;
    private final TokenService tokenService;

    public DatabaseMonitoringService(DatabaseMonitoringDao databaseMonitoringDao, OperationLogService operationLogService, TokenService tokenService) {
        this.databaseMonitoringDao = databaseMonitoringDao;
        this.operationLogService = operationLogService;
        this.tokenService = tokenService;
    }

    public void insert(JSONObject model, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        DatabaseMonitoring databaseMonitoring = MyUtils.model2Entity(model, DatabaseMonitoring.class);
        String nodeName = databaseMonitoring.getNodeName();
        try {
            databaseMonitoring.setId(MyUtils.generate32Id());
            tokenService.updateCreateIdAndTime(request, databaseMonitoring);
            databaseMonitoringDao.save(databaseMonitoring);

            operationLogService.setLog("数据库监控—添加", userName, "数据库运维-数据库监控", "添加了" + nodeName + "下运行管理平台的数据库监控", "1", request);
        } catch (Exception e) {
            operationLogService.setLog("数据库监控—添加", userName, "数据库运维-数据库监控", "添加了" + nodeName + "下运行管理平台的数据库监控", "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void delete(String id, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String nodeName = "";
        try {
            MyUtils.throwMsg(StringUtils.isEmpty(id), "id不能为空");
            DatabaseMonitoring databaseMonitoring = findById(id);
            nodeName = databaseMonitoring.getNodeName();
            databaseMonitoringDao.deleteById(id);
            operationLogService.setLog("数据库监控—删除", userName, "数据库运维-数据库监控", "删除了:" + nodeName + "下运行管理平台的数据库监控", "1", request);
        } catch (Exception e) {
            operationLogService.setLog("数据库监控—删除", userName, "数据库运维-数据库监控", "删除了" + nodeName + "下运行管理平台的数据库监控", "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void update(HttpServletRequest request, JSONObject model) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String nodeName = "";
        try {
            DatabaseMonitoring databaseMonitoring = MyUtils.model2Entity(model, DatabaseMonitoring.class);

            DatabaseMonitoring oldDatabaseMonitoring = findById(databaseMonitoring.getId());
            nodeName = oldDatabaseMonitoring.getNodeName();
            tokenService.updateUpdateIdAndTime(request, oldDatabaseMonitoring);
            oldDatabaseMonitoring.setRam(databaseMonitoring.getRam());
            oldDatabaseMonitoring.setRom(databaseMonitoring.getRom());
            oldDatabaseMonitoring.setAlarmWay(databaseMonitoring.getAlarmWay());
            oldDatabaseMonitoring.setPeopleNotified(databaseMonitoring.getPeopleNotified());
            databaseMonitoringDao.save(oldDatabaseMonitoring);
            operationLogService.setLog("数据库监控—编辑", userName, "数据库运维-数据库监控", "编辑了一个实例:" + nodeName + "下运行管理平台的数据库监控", "1", request);
        } catch (Exception e) {
            operationLogService.setLog("数据库监控—编辑", userName, "数据库运维-数据库监控", "编辑了一个实例:" + nodeName + "下运行管理平台的数据库监控", "0", request);
            throw new ValidException(e.getMessage());
        }

    }

    public Page<DatabaseMonitoring> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<DatabaseMonitoring> builder = new SpecificationBuilder<>();
        Specification<DatabaseMonitoring> specification = builder.init(params)
                .sContain("nodeName", "name")
                .dOrder("createTime")
                .build();
        return databaseMonitoringDao.findAll(specification, builder.getPageable());
    }

    public DatabaseMonitoring findById(String id) throws Exception {
        return databaseMonitoringDao.findById(id).orElseThrow(() -> new ValidException("该数据库监控不存在"));
    }

    public DatabaseMonitoring findByNodeIdAndDatabaseId (String nodeId,String databaseId) {
        return this.databaseMonitoringDao.findFirstByNodeIdAndDatabaseId(nodeId,databaseId);
    }

}
