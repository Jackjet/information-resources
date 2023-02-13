package d1.project.tangshan.operation.manage.service.database;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.remote.DatabaseBackupDao;
import d1.project.tangshan.operation.manage.entity.database.DatabaseBackup;
import d1.project.tangshan.operation.manage.entity.operations.module.Database;
import d1.project.tangshan.operation.manage.entity.operations.module.Node;
import d1.project.tangshan.operation.manage.service.operations.log.OperationLogService;
import d1.project.tangshan.operation.manage.service.operations.module.DatabaseService;
import d1.project.tangshan.operation.manage.service.operations.module.NodeService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import net.dcrun.component.postgre.PostgreService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.text.SimpleDateFormat;

/**
 * @author lin
 */
@Service
public class DatabaseBackupService {

    private final DatabaseBackupDao databaseBackupDao;
    private final OperationLogService operationLogService;
    private final TokenService tokenService;
    private final DatabaseService databaseService;
    private final NodeService nodeService;

    @Value("${auto.backup}")
    private String backup;
    @Value("${auto.recovery}")
    private String recovery;

    public DatabaseBackupService(DatabaseBackupDao databaseBackupDao, OperationLogService operationLogService, TokenService tokenService, DatabaseService databaseService, NodeService nodeService) {
        this.databaseBackupDao = databaseBackupDao;
        this.operationLogService = operationLogService;
        this.tokenService = tokenService;
        this.databaseService = databaseService;
        this.nodeService = nodeService;
    }

    public void insert(JSONObject model, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        DatabaseBackup databaseBackup = MyUtils.model2Entity(model, DatabaseBackup.class);
        String nodeName = "";
        String creatTime = "";
        String databaseName = "";
        try {

            Node node = nodeService.findById(databaseBackup.getNodeId());
            Database database = databaseService.findById(databaseBackup.getDatabaseId());
            String username = database.getUsername();
            String password = database.getPassword();
            tokenService.updateCreateIdAndTime(request, databaseBackup);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            creatTime = sdf.format(databaseBackup.getCreateTime().getTime());
            String id = MyUtils.generate32Id();
            String location = databaseBackup.getLocation();
            databaseBackup.setId(id);
            nodeName = databaseBackup.getNodeName();
            databaseName = databaseBackup.getDatabaseName();
            databaseBackup.setResult("2");
            databaseBackupDao.save(databaseBackup);
            switch (database.getType()) {
                case "Mysql":
                    backupPost(node.getIntranetIp(), id, "mysql", databaseName, username, password, location);
                    break;
                case "Postgresql":
                    backupPost(node.getIntranetIp(), id, "postgre", databaseName, username, password, location);
                    break;
                default:
                    throw new ValidException("数据库类型错误");
            }
            operationLogService.setLog("备份和恢复—添加", userName, "数据库运维-备份和恢复", creatTime + " 备份的" + nodeName + "下" + databaseName, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("备份和恢复—添加", userName, "数据库运维-备份和恢复", creatTime + " 备份的" + nodeName + "下" + databaseName, "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void delete(String id, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String nodeName = "";
        String creatTime = "";
        String databaseName = "";
        try {
            MyUtils.throwMsg(StringUtils.isEmpty(id), "id不能为空");
            DatabaseBackup databaseBackup = findById(id);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            creatTime = sdf.format(databaseBackup.getCreateTime().getTime());
            nodeName = databaseBackup.getNodeName();
            databaseName = databaseBackup.getDatabaseName();

            databaseBackupDao.deleteById(id);
            operationLogService.setLog("备份和恢复—删除", userName, "数据库运维-备份和恢复", "删除了" + creatTime + " 备份的" + nodeName + "下" + databaseName, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("备份和恢复—删除", userName, "数据库运维-备份和恢复", "删除了" + creatTime + " 备份的" + nodeName + "下" + databaseName, "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void recover(HttpServletRequest request, String id) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String nodeName = "";
        String creatTime = "";
        String databaseName = "";
        String logId = MyUtils.generate32Id();
        try {
            DatabaseBackup databaseBackup = findById(id);
            if (!"1".equals(databaseBackup.getResult())) {
                throw new ValidException("未成功备份不能恢复！");
            }
            Database database = databaseService.findById(databaseBackup.getDatabaseId());
            Node node = nodeService.findById(databaseBackup.getNodeId());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            creatTime = sdf.format(databaseBackup.getCreateTime().getTime());
            nodeName = databaseBackup.getNodeName();
            databaseName = databaseBackup.getDatabaseName();
            String location = databaseBackup.getLocation();
            nodeName = databaseBackup.getNodeName();
            databaseName = databaseBackup.getDatabaseName();
            String username = database.getUsername();
            String password = database.getPassword();
            databaseBackupDao.save(databaseBackup);
            operationLogService.setCtrlLog(logId, "备份和恢复—恢复", userName, "数据库运维-备份和恢复", "恢复了" + creatTime + " 备份的" + nodeName + "下" + databaseName, "", request);
            switch (database.getType()) {
                case "Mysql":
                    recoverPost(logId, node.getIntranetIp(), "mysql", databaseName, username, password, location);
                    break;
                case "Postgresql":
                    clearGet(node.getPublicIp(), database.getPort(), databaseName, username, password);
                    recoverPost(logId, node.getIntranetIp(), "postgre", databaseName, username, password, location);
                    break;
                default:
                    throw new ValidException("数据库类型错误");
            }
        } catch (Exception e) {
            operationLogService.setCtrlLog(logId, "备份和恢复—恢复", userName, "数据库运维-备份和恢复", "恢复了" + creatTime + " 备份的" + nodeName + "下" + databaseName, "0", request);
            e.printStackTrace();
            throw new ValidException("恢复失败" + e.getMessage());
        }

    }

    public Page<DatabaseBackup> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<DatabaseBackup> builder = new SpecificationBuilder<>();
        Specification<DatabaseBackup> specification = builder.init(params)
                .sEqual("nodeId", "nodeId")
                .sEqual("databaseId", "databaseId")
                .sEqual("instanceId", "instanceId")
                .dOrder("createTime")
                .build();
        return databaseBackupDao.findAll(specification, builder.getPageable());
    }

    public DatabaseBackup findById(String id) throws Exception {
        return databaseBackupDao.findById(id).orElseThrow(() -> new ValidException("该备份不存在"));
    }

    public void callback(JSONObject model) throws Exception {
        DatabaseBackup databaseBackup = MyUtils.model2Entity(model, DatabaseBackup.class);
        DatabaseBackup oldDatabaseBackup = findById(databaseBackup.getId());


        if (!StringUtils.isEmpty(databaseBackup.getLocation())) {
            oldDatabaseBackup.setLocation(databaseBackup.getLocation());
        }
        if (!StringUtils.isEmpty(databaseBackup.getResult())) {
            oldDatabaseBackup.setResult(databaseBackup.getResult());
        }
        if (!StringUtils.isEmpty(databaseBackup.getRemark())) {
            oldDatabaseBackup.setRemark(databaseBackup.getRemark());
        }
        databaseBackupDao.save(oldDatabaseBackup);
    }

    private void backupPost(String ip, String id, String type, String databaseName, String username, String password, String location) {
        RestTemplate restTemplate = new RestTemplate();
        String rootUrl = "http://" + ip + backup;
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("dbType", type);
        json.put("dbName", databaseName);
        json.put("dbUserName", username);
        json.put("dbPassword", password);
        json.put("location", location);
        restTemplate.postForObject(rootUrl, json, String.class);
    }

    private void recoverPost(String logId, String ip, String type, String databaseName, String username, String password, String location) {
        RestTemplate restTemplate = new RestTemplate();
        String rootUrl = "http://" + ip + recovery;
        JSONObject json = new JSONObject();
        json.put("id", logId);
        json.put("dbType", type);
        json.put("dbName", databaseName);
        json.put("dbUserName", username);
        json.put("dbPassword", password);
        json.put("location", location);
        restTemplate.postForObject(rootUrl, json, String.class);
    }

    private int clearGet(String ip, String port, String databaseName, String username, String password) throws Exception {
        try {
            PostgreService postgreService = new PostgreService();

            String dbUrl = "jdbc:postgresql://" + ip + ":" + port + "/" + databaseName + "?characterEncoding=utf8&useSSL=false&autoReconnect=true";
            Connection connect = postgreService.getConnection(dbUrl, username, password);
            String content = "DO $$ DECLARE\n" +
                    "    r RECORD;\n" +
                    "BEGIN\n" +
                    "    FOR r IN (SELECT tablename FROM pg_tables WHERE schemaname = current_schema()) LOOP\n" +
                    "        EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(r.tablename) || ' CASCADE';\n" +
                    "    END LOOP;\n" +
                    "END $$;";
            int flag = postgreService.execute(connect, content);
            postgreService.close(connect);
            return flag;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

    }


}
