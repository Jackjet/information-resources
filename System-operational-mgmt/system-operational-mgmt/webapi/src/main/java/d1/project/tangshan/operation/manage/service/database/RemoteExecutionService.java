package d1.project.tangshan.operation.manage.service.database;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.entity.WebAdminUser;
import d1.project.tangshan.operation.manage.entity.operations.Approval;
import d1.project.tangshan.operation.manage.entity.operations.module.Database;
import d1.project.tangshan.operation.manage.entity.operations.module.Node;
import d1.project.tangshan.operation.manage.service.WebAdminService;
import d1.project.tangshan.operation.manage.service.operations.ApprovalService;
import d1.project.tangshan.operation.manage.service.operations.log.OperationLogService;
import d1.project.tangshan.operation.manage.service.operations.module.DatabaseService;
import d1.project.tangshan.operation.manage.service.operations.module.NodeService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import net.dcrun.component.mysql.MySqlService;
import net.dcrun.component.postgre.PostgreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

/**
 * @author lin
 */
@Service
public class RemoteExecutionService {
    private final TokenService tokenService;
    @Autowired
    private ApprovalService approvalService;
    private final NodeService nodeService;
    private final DatabaseService databaseService;
    private final OperationLogService operationLogService;
    private final WebAdminService webAdminService;

    public RemoteExecutionService(TokenService tokenService, NodeService nodeService, DatabaseService databaseService, OperationLogService operationLogService, WebAdminService webAdminService) {
        this.tokenService = tokenService;
        this.nodeService = nodeService;
        this.databaseService = databaseService;
        this.operationLogService = operationLogService;
        this.webAdminService = webAdminService;
    }


    public String execute(JSONObject model, HttpServletRequest request) throws Exception {
        String userName;
        String userId;
        if (request == null) {
            userName = model.getString("userName");
            userId = model.getString("userId");
        } else {
            userName = tokenService.getUserByToken(request).getString("name");
            userId = tokenService.getUserByToken(request).getString("id");
        }

        Boolean isExecute = true;
        String nodeName = "未知";
        try {
            WebAdminUser webAdminUser = webAdminService.findById(userId).orElseThrow(() -> new ValidException("该用户不存在"));
            String content = model.getString("content");
            String lower = content.toLowerCase();
            Node node = nodeService.findById(model.getString("nodeId"));
            nodeName = node.getName();
            Database database = databaseService.findById(model.getString("databaseId"));
            if (lower.contains("delete") || lower.contains("update") || lower.contains("create") || lower.contains("insert")) {
                if (!"admin".equals(userId)) {
                    isExecute = false;
                    Approval approval = new Approval();
                    approval.setId(MyUtils.generate32Id());
                    approval.setStatus("stay");
                    approval.setType("sql");
                    approval.setApplicant(webAdminUser.getName());
                    approval.setTel(webAdminUser.getTel());
                    approval.setOrganization(webAdminUser.getOrganization());
                    JSONObject approvalContent = new JSONObject();
                    approvalContent.put("nodeId", node.getId());
                    approvalContent.put("databaseId", database.getId());
                    approvalContent.put("content", content);
                    approvalContent.put("userName", userName);
                    approvalContent.put("userId", userId);
                    approval.setContent(approvalContent.toJSONString());
                    tokenService.updateCreateIdAndTime(request, approval);
                    approvalService.insert(approval);
                    operationLogService.setLog("数据库运维—远程SQL", userName, "数据库运维-远程SQL", "申请了" + nodeName + "下运行管理系统的远程SQL", "1", userId);
                    throw new ValidException("您没有执行权限已为您提交执行权限申请");
                }
            }
            String ip = node.getPublicIp();
            String port = database.getPort();
            String databaseName = database.getName();
            String username = database.getUsername();
            String password = database.getPassword();
            Integer flag = 0;
            switch (database.getType()) {
                case "Postgresql":
                    if (lower.contains("delete") || lower.contains("update") || lower.contains("create") || lower.contains("insert")) {
                        flag = connectPostgreSql(ip, port, databaseName, username, password, content);
                        operationLogService.setLog("数据库运维—远程SQL", userName, "数据库运维-远程SQL", "执行了" + nodeName + "下运行管理系统的远程SQL", "1", userId);
                        return flag.toString();
                    } else if (lower.contains("select")) {
                        List<Map<String, Object>> resultList = connectPostgreSqlQuery(ip, port, databaseName, username, password, content);
                        operationLogService.setLog("数据库运维—远程SQL", userName, "数据库运维-远程SQL", "执行了" + nodeName + "下运行管理系统的远程SQL", "1", userId);
                        return JSONObject.toJSONString(resultList);
                    } else {
                        throw new ValidException("执行失败");
                    }
                case "Mysql":
                    if (lower.contains("delete") || lower.contains("update") || lower.contains("create") || lower.contains("insert")) {
                        flag = connectMysql(ip, port, databaseName, username, password, content);
                        operationLogService.setLog("数据库运维—远程SQL", userName, "数据库运维-远程SQL", "执行了" + nodeName + "下运行管理系统的远程SQL", "1", userId);
                        return flag.toString();
                    } else if (lower.contains("select")) {
                        List<Map<String, Object>> resultList = connectMysqlQuery(ip, port, databaseName, username, password, content);
                        operationLogService.setLog("数据库运维—远程SQL", userName, "数据库运维-远程SQL", "执行了" + nodeName + "下运行管理系统的远程SQL", "1", userId);
                        return JSONObject.toJSONString(resultList);
                    } else {
                        throw new ValidException("执行失败");
                    }
                default:
                    throw new ValidException("数据库类型错误");
            }
        } catch (Exception e) {
            if (isExecute) {
                operationLogService.setLog("数据库运维—远程SQL", userName, "数据库运维-远程SQL", "执行了" + nodeName + "下运行管理系统的远程SQL", "0", userId);
            }
            e.printStackTrace();
            throw new ValidException(e.getMessage());
        }
    }

    public void executeApproval(JSONObject model, HttpServletRequest request) throws Exception {
        String userName;
        String userId;
        if (request == null) {
            userName = model.getString("userName");
            userId = model.getString("userId");
        } else {
            userName = tokenService.getUserByToken(request).getString("name");
            userId = tokenService.getUserByToken(request).getString("id");
        }

        String nodeName = "未知";
        try {
            String content = model.getString("content");
            String lower = content.toLowerCase();
            Node node = nodeService.findById(model.getString("nodeId"));
            nodeName = node.getName();
            Database database = databaseService.findById(model.getString("databaseId"));

//            String ip = node.getPublicIp();
            // 使用内网IP
            String ip = node.getIntranetIp();

            String port = database.getPort();
            String databaseName = database.getName();
            String username = database.getUsername();
            String password = database.getPassword();
            switch (database.getType()) {
                case "Postgresql":
                    if (lower.contains("delete") || lower.contains("update") || lower.contains("create") || lower.contains("insert")) {
                        connectPostgreSql(ip, port, databaseName, username, password, content);
                        operationLogService.setLog("数据库运维—远程SQL", userName, "数据库运维-远程SQL", "执行了" + nodeName + "下运行管理系统的远程SQL", "1", userId);
                    } else if (lower.contains("select")) {
                        List<Map<String, Object>> resultList = connectPostgreSqlQuery(ip, port, databaseName, username, password, content);
                        operationLogService.setLog("数据库运维—远程SQL", userName, "数据库运维-远程SQL", "执行了" + nodeName + "下运行管理系统的远程SQL", "1", userId);
                    }
                    break;
                case "Mysql":
                    if (lower.contains("delete") || lower.contains("update") || lower.contains("create") || lower.contains("insert")) {
                        connectMysql(ip, port, databaseName, username, password, content);
                        operationLogService.setLog("数据库运维—远程SQL", userName, "数据库运维-远程SQL", "执行了" + nodeName + "下运行管理系统的远程SQL", "1", userId);
                    } else if (lower.contains("select")) {
                        List<Map<String, Object>> resultList = connectMysqlQuery(ip, port, databaseName, username, password, content);
                        operationLogService.setLog("数据库运维—远程SQL", userName, "数据库运维-远程SQL", "执行了" + nodeName + "下运行管理系统的远程SQL", "1", userId);
                    }
                    break;
                default:
                    throw new ValidException("数据库类型错误");
            }
        } catch (Exception e) {
            operationLogService.setLog("数据库运维—远程SQL", userName, "数据库运维-远程SQL", "执行了" + nodeName + "下运行管理系统的远程SQL", "0", userId);
        }
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
            System.out.println(content);
            int flag = mySqlService.execute(connect, content);
            mySqlService.close(connect);
            return flag;
        } catch (Exception e) {
            throw new ValidException(e.getMessage());
        }
    }

    private List<Map<String, Object>> connectMysqlQuery(String ip, String port, String databaseName, String username, String password, String content) throws Exception {
        try {
            MySqlService mySqlService = new MySqlService();
            String dbUrl = "jdbc:mysql://" + ip + ":" + port + "/" + databaseName + "?&useUnicode=true&characterEncoding=UTF8&useSSL=false&autoReconnect=true";
//            dbUrl = "jdbc:mysql://10.113.0.32:3306/authentication?characterEncoding=utf8&useSSL=false&autoReconnect=true";

            Connection connect = mySqlService.getConnection(dbUrl, username, password);
            List<Map<String, Object>> flag = mySqlService.executeQuery(connect, content);
            mySqlService.close(connect);
            return flag;
        } catch (Exception e) {
            e.printStackTrace();
            throw new ValidException(e.getMessage());
        }
    }

    private List<Map<String, Object>> connectPostgreSqlQuery(String ip, String port, String databaseName, String username, String password, String content) throws Exception {
        try {
            PostgreService postgreService = new PostgreService();
            String dbUrl = "jdbc:postgresql://" + ip + ":" + port + "/" + databaseName + "?characterEncoding=utf8&useSSL=false&autoReconnect=true";

            Connection connect = postgreService.getConnection(dbUrl, username, password);
            List<Map<String, Object>> flag = postgreService.executeQuery(connect, content);
            postgreService.close(connect);
            return flag;
        } catch (Exception e) {
            throw new ValidException(e.getMessage());
        }
    }


}
