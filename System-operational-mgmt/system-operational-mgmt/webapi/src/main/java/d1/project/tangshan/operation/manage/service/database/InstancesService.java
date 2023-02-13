package d1.project.tangshan.operation.manage.service.database;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.remote.InstancesDao;
import d1.project.tangshan.operation.manage.entity.database.Instances;
import d1.project.tangshan.operation.manage.entity.database.ParameterConfig;
import d1.project.tangshan.operation.manage.entity.operations.module.Database;
import d1.project.tangshan.operation.manage.entity.operations.module.Node;
import d1.project.tangshan.operation.manage.service.operations.log.OperationLogService;
import d1.project.tangshan.operation.manage.service.operations.module.DatabaseService;
import d1.project.tangshan.operation.manage.service.operations.module.NodeService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import net.dcrun.component.mysql.MySqlService;
import net.dcrun.component.postgre.PostgreService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.util.List;

/**
 * @author lin
 */
@Service
public class InstancesService {
    private final InstancesDao instancesDao;
    private final OperationLogService operationLogService;
    private final TokenService tokenService;
    private final NodeService nodeService;
    private final DatabaseService databaseService;
    private final ParameterConfigService parameterConfigService;

    public InstancesService(InstancesDao instancesDao, OperationLogService operationLogService, TokenService tokenService, NodeService nodeService, DatabaseService databaseService, ParameterConfigService parameterConfigService) {
        this.instancesDao = instancesDao;
        this.operationLogService = operationLogService;
        this.tokenService = tokenService;
        this.nodeService = nodeService;
        this.databaseService = databaseService;
        this.parameterConfigService = parameterConfigService;
    }

    public void insert(JSONObject model, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        Instances instances = MyUtils.model2Entity(model, Instances.class);
        String name = instances.getName();
        try {

            MyUtils.throwMsg(instancesDao.existsByName(instances.getName()), "实例名称已存在");
            instances.setId(MyUtils.generate32Id());
            tokenService.updateCreateIdAndTime(request, instances);
            Node node = nodeService.findById(instances.getNodeId());
            Database database = databaseService.findById(instances.getDatabaseId());
            ParameterConfig parameterConfig = parameterConfigService.findById(instances.getConfigId());
            String ip = node.getPublicIp();
            String port = database.getPort();
            String databaseName = database.getName();
            String username = database.getUsername();
            String password = database.getPassword();
            String content = parameterConfig.getContent();
            JSONArray arr = JSONObject.parseArray(content);
            StringBuilder sql = new StringBuilder("create table ").append(name).append("( ");
            for (int i = 0; i < arr.size(); i++) {
                sql.append(arr.getJSONObject(i).get("name")).append(" ");
                sql.append(arr.getJSONObject(i).get("type"));
                if (StringUtils.isEmpty(arr.getJSONObject(i).get("length"))) {
                    sql.append("(").append(arr.getJSONObject(i).get("length")).append(") ");
                }
                if (!StringUtils.isEmpty(arr.getJSONObject(i).get("allowNull")) && !arr.getJSONObject(i).getBoolean("allowNull")) {
                    sql.append(" NOT NULL ");
                }
                if (!StringUtils.isEmpty(arr.getJSONObject(i).get("isPrimary")) && arr.getJSONObject(i).getBoolean("isPrimary")) {
                    sql.append(" PRIMARY KEY ");
                }
                sql.append(",");
            }
            sql.deleteCharAt(sql.length() - 1);
            sql.append(");");
            System.out.println(sql);
            switch (database.getType()) {
                case "Postgresql":

                    try {
                        connectPostgreSql(ip, port, databaseName, username, password, sql.toString());
                    } catch (Exception e) {
                        throw new ValidException("添加失败");
                    }
                    break;
                case "Mysql":
                    try {
                        connectMysql(ip, port, databaseName, username, password, sql.toString());
                    } catch (Exception e) {
                        throw new ValidException("添加失败");
                    }
                    break;
                default:
                    throw new ValidException("数据库类型错误");
            }
            instancesDao.save(instances);
            operationLogService.setLog("实例管理—添加", userName, "数据库运维-实例管理", "添加了一个实例:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("实例管理—添加", userName, "数据库运维-实例管理", "添加了一个实例:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void delete(String id, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            MyUtils.throwMsg(StringUtils.isEmpty(id), "id不能为空");
            Instances instances = findById(id);
            name = instances.getName();
            instancesDao.deleteById(id);
            operationLogService.setLog("实例管理—删除", userName, "数据库运维-实例管理", "删除了一个实例:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("实例管理—删除", userName, "数据库运维-实例管理", "删除了一个实例:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void update(HttpServletRequest request, JSONObject model) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            Instances instances = MyUtils.model2Entity(model, Instances.class);

            Instances oldInstances = findById(instances.getId());

            tokenService.updateUpdateIdAndTime(request, oldInstances);
            name = oldInstances.getName();
            oldInstances.setConfigId(instances.getConfigId());
            oldInstances.setConfigName(instances.getConfigName());
            oldInstances.setRemark(instances.getRemark());

            instancesDao.save(oldInstances);
            operationLogService.setLog("实例管理—编辑", userName, "数据库运维-实例管理", "编辑了一个实例:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("实例管理—编辑", userName, "数据库运维-实例管理", "编辑了一个实例:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }

    }

    public Page<Instances> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<Instances> builder = new SpecificationBuilder<>();
        Specification<Instances> specification = builder.init(params)
                .sContain("name", "name")
                .sEqual("nodeId", "nodeId")
                .sEqual("databaseId", "databaseId")
                .dOrder("createTime")
                .build();
        return instancesDao.findAll(specification, builder.getPageable());
    }

    public Instances findById(String id) throws Exception {
        return instancesDao.findById(id).orElseThrow(() -> new ValidException("该实例不存在"));
    }

    public List<Instances> findAllByNodeIdAndDatabaseId(String nodeId, String databaseId) {
        return instancesDao.findAllByNodeIdAndDatabaseIdOrderByCreateTimeDesc(nodeId, databaseId);
    }

    private int connectPostgreSql(String ip, String port, String databaseName, String username, String password, String content) throws Exception {
        try {
            PostgreService postgreService = new PostgreService();
            String dbUrl = "jdbc:postgresql://" + ip + ":" + port + "/" + databaseName + "?characterEncoding=utf8&useSSL=false&autoReconnect=true";
            Connection connect = postgreService.getConnection(dbUrl, username, password);
            int flag = postgreService.execute(connect, content);
            postgreService.close(connect);
            return flag;
        } catch (Exception e) {
            throw new ValidException(e.getMessage());
        }
    }

    private int connectMysql(String ip, String port, String databaseName, String username, String password, String content) throws Exception {
        try {
            MySqlService mySqlService = new MySqlService();
            String dbUrl = "jdbc:mysql://" + ip + ":" + port + "/" + databaseName + "?&useUnicode=true&characterEncoding=UTF8&useSSL = false";
            Connection connect = mySqlService.getConnection(dbUrl, username, password);
            int flag = mySqlService.execute(connect, content);
            mySqlService.close(connect);
            return flag;
        } catch (Exception e) {
            throw new ValidException(e.getMessage());
        }

    }
}
