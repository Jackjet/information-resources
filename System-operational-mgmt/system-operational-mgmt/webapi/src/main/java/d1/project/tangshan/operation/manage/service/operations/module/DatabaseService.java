package d1.project.tangshan.operation.manage.service.operations.module;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.operations.module.DatabaseDao;
import d1.project.tangshan.operation.manage.entity.operations.module.Database;
import d1.project.tangshan.operation.manage.service.operations.log.OperationLogService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lin
 */
@Service
public class DatabaseService {
    private final DatabaseDao databaseDao;
    private final OperationLogService operationLogService;
    private final TokenService tokenService;

    public DatabaseService(DatabaseDao databaseDao, OperationLogService operationLogService, TokenService tokenService) {
        this.databaseDao = databaseDao;
        this.operationLogService = operationLogService;
        this.tokenService = tokenService;
    }


    public void insert(JSONObject model, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        Database database = MyUtils.model2Entity(model, Database.class);
        String name = database.getName();
        try {

            MyUtils.throwMsg(databaseDao.existsByName(database.getName()), "数据库名称已存在");
            //MyUtils.throwMsg(databaseDao.existsByNodeIdAndPort(database.getNodeId(), database.getPort()), "该服务节点下端口已存在");
            database.setId(MyUtils.generate32Id());
            tokenService.updateCreateIdAndTime(request, database);

            databaseDao.save(database);
            operationLogService.setLog("数据库管理—添加", userName, "运维行为管理-组件管理-数据库管理", "添加了一个数据库:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("数据库管理—添加", userName, "运维行为管理-组件管理-数据库管理", "添加了一个数据库:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void delete(String id, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            MyUtils.throwMsg(StringUtils.isEmpty(id), "id不能为空");
            Database database = databaseDao.findById(id).orElseThrow(() -> new ValidException("该数据库不存在"));
            name = database.getName();
            databaseDao.deleteById(id);
            operationLogService.setLog("数据库管理—删除", userName, "运维行为管理-组件管理-数据库管理", "删除了一个数据库:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("数据库管理—删除", userName, "运维行为管理-组件管理-数据库管理", "删除了一个数据库:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void update(HttpServletRequest request, JSONObject model) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            Database database = MyUtils.model2Entity(model, Database.class);

            Database oldDatabase = databaseDao.findById(database.getId()).orElseThrow(() -> new ValidException("该数据库不存在"));
            name = oldDatabase.getName();
            tokenService.updateUpdateIdAndTime(request, oldDatabase);
            if (!database.getPort().equals(oldDatabase.getPort())) {
                //MyUtils.throwMsg(databaseDao.existsByNodeIdAndPort(oldDatabase.getNodeId(), database.getPort()), "该服务节点下端口已存在");
                oldDatabase.setPort(database.getPort());
            }

            oldDatabase.setProcessName(database.getProcessName());
            oldDatabase.setSystemAndPlatform(database.getSystemAndPlatform());
            oldDatabase.setUsername(database.getUsername());
            oldDatabase.setPassword(database.getPassword());
            oldDatabase.setRemark(database.getRemark());
            databaseDao.save(oldDatabase);
            operationLogService.setLog("数据库管理—编辑", userName, "运维行为管理-组件管理-数据库管理", "编辑了一个数据库:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("数据库管理—编辑", userName, "运维行为管理-组件管理-数据库管理", "编辑了一个数据库:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }

    }

    public Page<Database> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<Database> builder = new SpecificationBuilder<>();
        Specification<Database> specification = builder.init(params)
                .sContain("name", "name")
                .sEqual("nodeId", "nodeId")
                .sContain("systemAndPlatform", "systemAndPlatform")
                .dOrder("createTime")
                .build();
        return databaseDao.findAll(specification, builder.getPageable());
    }

    public Database findById(String id) throws Exception {
        return databaseDao.findById(id).orElseThrow(() -> new ValidException("该数据库不存在"));
    }

    public List<Database> findAllByNodeId(String nodeId){
        return databaseDao.findAllByNodeIdOrderByCreateTimeDesc(nodeId);
    }
}
